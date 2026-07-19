package com.telcox.billingservice.dto;

import com.telcox.billingservice.domain.Invoice;
import com.telcox.billingservice.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record InvoiceResponse(UUID id, UUID subscriptionId, UUID customerId, String billingPeriod,
                               BigDecimal monthlyFee, BigDecimal overageFee, BigDecimal taxAmount,
                               BigDecimal totalAmount, InvoiceStatus status) {
    public static InvoiceResponse from(Invoice i) {
        return new InvoiceResponse(i.getId(), i.getSubscriptionId(), i.getCustomerId(), i.getBillingPeriod(),
                i.getMonthlyFee(), i.getOverageFee(), i.getTaxAmount(), i.getTotalAmount(), i.getStatus());
    }
}
