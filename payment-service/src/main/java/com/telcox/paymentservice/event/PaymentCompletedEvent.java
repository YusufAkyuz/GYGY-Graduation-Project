package com.telcox.paymentservice.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedEvent(UUID orderId, UUID paymentId, UUID customerId, String productCode,
                                     Instant occurredAt) {
    public PaymentCompletedEvent(UUID orderId, UUID paymentId, UUID customerId, String productCode) {
        this(orderId, paymentId, customerId, productCode, Instant.now());
    }
}
