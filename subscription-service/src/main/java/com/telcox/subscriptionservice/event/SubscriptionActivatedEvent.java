package com.telcox.subscriptionservice.event;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionActivatedEvent(UUID orderId, UUID subscriptionId, String msisdn, Instant occurredAt) {
    public SubscriptionActivatedEvent(UUID orderId, UUID subscriptionId, String msisdn) {
        this(orderId, subscriptionId, msisdn, Instant.now());
    }
}
