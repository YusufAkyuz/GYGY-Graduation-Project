package com.telcox.paymentservice.dto;

import com.telcox.paymentservice.domain.Payment;
import com.telcox.paymentservice.domain.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(UUID id, UUID orderId, BigDecimal amount, String currency, PaymentStatus status) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getOrderId(), payment.getAmount(),
                payment.getCurrency(), payment.getStatus());
    }
}
