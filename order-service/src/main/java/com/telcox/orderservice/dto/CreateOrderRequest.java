package com.telcox.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotBlank String productCode
) {
}
