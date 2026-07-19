package com.telcox.paymentservice.event;

import java.time.Instant;
import java.util.UUID;

public record PaymentFailedEvent(UUID orderId, String reason, Instant occurredAt) {
    public PaymentFailedEvent(UUID orderId, String reason) {
        this(orderId, reason, Instant.now());
    }
}
