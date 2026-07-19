package com.telcox.subscriptionservice.dto;

import com.telcox.subscriptionservice.domain.Subscription;
import com.telcox.subscriptionservice.domain.SubscriptionStatus;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionResponse(UUID id, UUID customerId, UUID orderId, String productCode, String msisdn,
                                    SubscriptionStatus status, Instant activatedAt) {
    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(subscription.getId(), subscription.getCustomerId(), subscription.getOrderId(),
                subscription.getProductCode(), subscription.getMsisdn(), subscription.getStatus(),
                subscription.getActivatedAt());
    }
}
