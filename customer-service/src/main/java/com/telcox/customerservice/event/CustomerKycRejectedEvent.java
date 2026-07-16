package com.telcox.customerservice.event;

import java.time.Instant;
import java.util.UUID;

public record CustomerKycRejectedEvent(UUID customerId, String reason, Instant occurredAt) {
    public CustomerKycRejectedEvent(UUID customerId, String reason) {
        this(customerId, reason, Instant.now());
    }
}
