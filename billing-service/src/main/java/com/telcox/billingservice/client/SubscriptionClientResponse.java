package com.telcox.billingservice.client;

import java.util.UUID;

public record SubscriptionClientResponse(UUID id, UUID customerId, String productCode, String msisdn) {
}
