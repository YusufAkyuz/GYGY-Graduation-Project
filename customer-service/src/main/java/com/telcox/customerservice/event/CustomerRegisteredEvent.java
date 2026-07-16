package com.telcox.customerservice.event;

import java.time.Instant;
import java.util.UUID;

public record CustomerRegisteredEvent(UUID customerId, String email, Instant occurredAt) {
    public CustomerRegisteredEvent(UUID customerId, String email) {
        this(customerId, email, Instant.now());
    }
}
