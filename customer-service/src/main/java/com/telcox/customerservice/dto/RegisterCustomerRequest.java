package com.telcox.customerservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record RegisterCustomerRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "TCKN 11 haneli olmalıdır") String tckn,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotEmpty @Valid List<AddressDto> addresses
) {
}
