package com.telcox.orderservice.client;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductClientResponse(UUID id, String code, BigDecimal monthlyPrice, String currency, Instant effectiveTo) {

    public boolean isCurrent() {
        return effectiveTo == null;
    }
}
