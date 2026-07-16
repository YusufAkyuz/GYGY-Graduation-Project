package com.telcox.customerservice.event;

import java.time.Instant;
import java.util.UUID;

public record CustomerKycApprovedEvent(UUID customerId, Instant occurredAt) {
    public CustomerKycApprovedEvent(UUID customerId) {
        this(customerId, Instant.now());
    }
}
