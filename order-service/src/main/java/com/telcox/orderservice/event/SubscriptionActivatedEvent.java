package com.telcox.orderservice.event;

import java.util.UUID;

public record SubscriptionActivatedEvent(UUID orderId, UUID subscriptionId, String msisdn) {
}
