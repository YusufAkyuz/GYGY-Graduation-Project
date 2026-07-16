package com.telcox.productcatalogservice.dto;

import com.telcox.productcatalogservice.domain.ProductType;
import com.telcox.productcatalogservice.domain.SubscriberClass;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String code,
        @NotBlank String name,
        String description,
        @NotNull ProductType productType,
        @NotNull SubscriberClass subscriberClass,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal monthlyPrice,
        String currency,
        String targetSegment
) {
}
