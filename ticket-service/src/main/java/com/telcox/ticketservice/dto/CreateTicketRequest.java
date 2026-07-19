package com.telcox.ticketservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTicketRequest(
        @NotNull UUID customerId,
        @NotBlank String subject,
        @NotBlank String description
) {
}
