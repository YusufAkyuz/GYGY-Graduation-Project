package com.telcox.customerservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RejectKycRequest(
        @NotBlank String reason
) {
}
