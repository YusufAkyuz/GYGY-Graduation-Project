package com.telcox.billingservice.event;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceGeneratedEvent(UUID invoiceId, UUID customerId, UUID subscriptionId, BigDecimal totalAmount,
                                     String billingPeriod) {
}
