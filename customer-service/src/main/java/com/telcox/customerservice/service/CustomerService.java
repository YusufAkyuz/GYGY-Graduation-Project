package com.telcox.customerservice.service;

import com.telcox.common.audit.AuditService;
import com.telcox.common.error.ApiException;
import com.telcox.common.outbox.OutboxEventPublisherService;
import com.telcox.common.util.Sha256Hasher;
import com.telcox.common.util.TcknValidator;
import com.telcox.customerservice.domain.Address;
import com.telcox.customerservice.domain.Customer;
import com.telcox.customerservice.domain.CustomerStatus;
import com.telcox.customerservice.dto.AddressDto;
import com.telcox.customerservice.dto.RegisterCustomerRequest;
import com.telcox.customerservice.event.CustomerKycApprovedEvent;
import com.telcox.customerservice.event.CustomerKycRejectedEvent;
import com.telcox.customerservice.event.CustomerRegisteredEvent;
import com.telcox.customerservice.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerService {

    private static final String AGGREGATE_TYPE = "Customer";

    private final CustomerRepository customerRepository;
    private final OutboxEventPublisherService outboxEventPublisherService;
    private final AuditService auditService;

    public CustomerService(CustomerRepository customerRepository,
                            OutboxEventPublisherService outboxEventPublisherService,
                            AuditService auditService) {
        this.customerRepository = customerRepository;
        this.outboxEventPublisherService = outboxEventPublisherService;
        this.auditService = auditService;
    }

    @Transactional
    public Customer register(RegisterCustomerRequest request, String actorUserId) {
        if (!TcknValidator.isValid(request.tckn())) {
            throw ApiException.badRequest("TCKN geçersiz (checksum hatası)");
        }

        String tcknHash = Sha256Hasher.hash(request.tckn());
        if (customerRepository.existsByTcknHash(tcknHash)) {
            throw ApiException.conflict("Bu TCKN ile kayıtlı bir müşteri zaten var");
        }
        if (customerRepository.existsByEmail(request.email())) {
            throw ApiException.conflict("Bu e-posta ile kayıtlı bir müşteri zaten var");
        }

        Customer customer = new Customer(request.firstName(), request.lastName(),
                request.tckn(), tcknHash, request.email(), request.phone());
        for (AddressDto addressDto : request.addresses()) {
            customer.addAddress(new Address(addressDto.addressType(), addressDto.addressLine(),
                    addressDto.city(), addressDto.postalCode(), addressDto.country()));
        }

        customer = customerRepository.save(customer);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, customer.getId().toString(),
                "CustomerRegistered", new CustomerRegisteredEvent(customer.getId(), customer.getEmail()));
        auditService.record(AGGREGATE_TYPE, customer.getId().toString(), "REGISTERED", actorUserId,
                "Müşteri kaydı oluşturuldu, KYC durumu PENDING");

        return customer;
    }

    @Transactional(readOnly = true)
    public Customer getActiveById(UUID id) {
        return customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> ApiException.notFound("Müşteri bulunamadı: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Customer> list(Pageable pageable) {
        return customerRepository.findAllByDeletedFalse(pageable);
    }

    @Transactional
    public Customer approveKyc(UUID id, String actorUserId) {
        Customer customer = getActiveById(id);
        requirePending(customer);
        customer.approveKyc();

        outboxEventPublisherService.publish(AGGREGATE_TYPE, id.toString(),
                "CustomerKYCApproved", new CustomerKycApprovedEvent(id));
        auditService.record(AGGREGATE_TYPE, id.toString(), "KYC_APPROVED", actorUserId, null);

        return customer;
    }

    @Transactional
    public Customer rejectKyc(UUID id, String reason, String actorUserId) {
        Customer customer = getActiveById(id);
        requirePending(customer);
        customer.rejectKyc(reason);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, id.toString(),
                "CustomerKYCRejected", new CustomerKycRejectedEvent(id, reason));
        auditService.record(AGGREGATE_TYPE, id.toString(), "KYC_REJECTED", actorUserId, reason);

        return customer;
    }

    @Transactional
    public void softDelete(UUID id, String actorUserId) {
        Customer customer = getActiveById(id);
        customer.softDelete();
        auditService.record(AGGREGATE_TYPE, id.toString(), "SOFT_DELETED", actorUserId,
                "KVKK/GDPR uyumu için soft-delete uygulandı");
    }

    private void requirePending(Customer customer) {
        if (customer.getStatus() != CustomerStatus.PENDING) {
            throw ApiException.conflict("Müşteri zaten " + customer.getStatus() + " durumunda");
        }
    }
}
