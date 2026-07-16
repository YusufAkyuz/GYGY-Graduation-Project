package com.telcox.productcatalogservice.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TariffCreatedEvent(UUID productId, String code, BigDecimal monthlyPrice, Instant occurredAt) {
    public TariffCreatedEvent(UUID productId, String code, BigDecimal monthlyPrice) {
        this(productId, code, monthlyPrice, Instant.now());
    }
}
