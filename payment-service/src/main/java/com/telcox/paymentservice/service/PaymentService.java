package com.telcox.paymentservice.service;

import com.telcox.common.outbox.OutboxEventPublisherService;
import com.telcox.paymentservice.domain.Payment;
import com.telcox.paymentservice.domain.PaymentStatus;
import com.telcox.paymentservice.event.OrderCreatedEvent;
import com.telcox.paymentservice.event.PaymentCompletedEvent;
import com.telcox.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mock PSP (FR-25): MVP'de gerçek bir ödeme sağlayıcısına bağlanmaz, ödeme daima başarılı kabul
 * edilir. İdempotency, order_id üzerindeki unique constraint ile sağlanır (FR-26).
 */
@Service
public class PaymentService {

    private static final String AGGREGATE_TYPE = "Payment";

    private final PaymentRepository paymentRepository;
    private final OutboxEventPublisherService outboxEventPublisherService;

    public PaymentService(PaymentRepository paymentRepository, OutboxEventPublisherService outboxEventPublisherService) {
        this.paymentRepository = paymentRepository;
        this.outboxEventPublisherService = outboxEventPublisherService;
    }

    @Transactional
    public void processPayment(OrderCreatedEvent event) {
        if (paymentRepository.existsByOrderId(event.orderId())) {
            return; // idempotent: bu sipariş için ödeme zaten işlenmiş
        }

        Payment payment = new Payment(event.orderId(), event.amount(), event.currency(), PaymentStatus.COMPLETED, null);
        payment = paymentRepository.save(payment);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, payment.getId().toString(), "PaymentCompleted",
                new PaymentCompletedEvent(event.orderId(), payment.getId(), event.customerId(), event.productCode()));
    }
}
