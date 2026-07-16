package com.telcox.customerservice.dto;

import com.telcox.customerservice.domain.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDto(
        @NotNull AddressType addressType,
        @NotBlank String addressLine,
        @NotBlank String city,
        @NotBlank String postalCode,
        @NotBlank String country
) {
}
