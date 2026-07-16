package com.telcox.productcatalogservice.dto;

import com.telcox.productcatalogservice.domain.Product;
import com.telcox.productcatalogservice.domain.ProductType;
import com.telcox.productcatalogservice.domain.SubscriberClass;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String code,
        String name,
        String description,
        ProductType productType,
        SubscriberClass subscriberClass,
        BigDecimal monthlyPrice,
        String currency,
        String targetSegment,
        int versionNumber,
        Instant effectiveFrom,
        Instant effectiveTo
) implements Serializable {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getDescription(),
                product.getProductType(),
                product.getSubscriberClass(),
                product.getMonthlyPrice(),
                product.getCurrency(),
                product.getTargetSegment(),
                product.getVersionNumber(),
                product.getEffectiveFrom(),
                product.getEffectiveTo()
        );
    }
}
