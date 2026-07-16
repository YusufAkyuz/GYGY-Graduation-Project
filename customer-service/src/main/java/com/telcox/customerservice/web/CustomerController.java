package com.telcox.customerservice.web;

import com.telcox.common.web.PageResponse;
import com.telcox.customerservice.domain.Customer;
import com.telcox.customerservice.dto.CustomerResponse;
import com.telcox.customerservice.dto.RegisterCustomerRequest;
import com.telcox.customerservice.dto.RejectKycRequest;
import com.telcox.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse register(@Valid @RequestBody RegisterCustomerRequest request, Authentication authentication) {
        String actorUserId = authentication != null ? authentication.getName() : "self-service";
        Customer customer = customerService.register(request, actorUserId);
        return CustomerResponse.from(customer);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public CustomerResponse get(@PathVariable UUID id) {
        return CustomerResponse.from(customerService.getActiveById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public PageResponse<CustomerResponse> list(Pageable pageable) {
        return PageResponse.from(customerService.list(pageable).map(CustomerResponse::from));
    }

    @PostMapping("/{id}/kyc/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerResponse approveKyc(@PathVariable UUID id, Authentication authentication) {
        return CustomerResponse.from(customerService.approveKyc(id, authentication.getName()));
    }

    @PostMapping("/{id}/kyc/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerResponse rejectKyc(@PathVariable UUID id, @Valid @RequestBody RejectKycRequest request,
                                       Authentication authentication) {
        return CustomerResponse.from(customerService.rejectKyc(id, request.reason(), authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        customerService.softDelete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
