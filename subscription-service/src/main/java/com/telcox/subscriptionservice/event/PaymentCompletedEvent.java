package com.telcox.subscriptionservice.event;

import java.util.UUID;

public record PaymentCompletedEvent(UUID orderId, UUID paymentId, UUID customerId, String productCode) {
}
