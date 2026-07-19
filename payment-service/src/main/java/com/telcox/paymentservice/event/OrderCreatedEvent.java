package com.telcox.paymentservice.event;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEvent(UUID orderId, UUID customerId, String productCode, UUID productVersionId,
                                 BigDecimal amount, String currency) {
}
