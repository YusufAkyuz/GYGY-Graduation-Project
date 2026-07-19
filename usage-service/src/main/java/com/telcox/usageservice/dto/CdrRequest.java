package com.telcox.usageservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/** CDR simülatörü tarafından üretilen bir kullanım kaydı (FR-17). */
public record CdrRequest(
        @NotNull UUID subscriptionId,
        @NotBlank String msisdn,
        @NotNull @DecimalMin("0.0") BigDecimal dataMb
) {
}
