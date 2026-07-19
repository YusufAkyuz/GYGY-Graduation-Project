package com.telcox.orderservice.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(UUID orderId, UUID customerId, String productCode, UUID productVersionId,
                                 BigDecimal amount, String currency, Instant occurredAt) {
    public OrderCreatedEvent(UUID orderId, UUID customerId, String productCode, UUID productVersionId,
                              BigDecimal amount, String currency) {
        this(orderId, customerId, productCode, productVersionId, amount, currency, Instant.now());
    }
}
