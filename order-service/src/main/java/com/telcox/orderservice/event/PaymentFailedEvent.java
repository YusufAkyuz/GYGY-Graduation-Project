package com.telcox.orderservice.event;

import java.util.UUID;

public record PaymentFailedEvent(UUID orderId, String reason) {
}
