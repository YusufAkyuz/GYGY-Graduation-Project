package com.telcox.productcatalogservice.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TariffPriceChangedEvent(String code, UUID previousProductId, UUID newProductId,
                                       BigDecimal oldPrice, BigDecimal newPrice, Instant occurredAt) {
    public TariffPriceChangedEvent(String code, UUID previousProductId, UUID newProductId,
                                    BigDecimal oldPrice, BigDecimal newPrice) {
        this(code, previousProductId, newProductId, oldPrice, newPrice, Instant.now());
    }
}
