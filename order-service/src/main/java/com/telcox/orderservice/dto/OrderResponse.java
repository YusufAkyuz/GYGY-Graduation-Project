package com.telcox.orderservice.dto;

import com.telcox.orderservice.domain.Order;
import com.telcox.orderservice.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        String productCode,
        BigDecimal amount,
        String currency,
        OrderStatus status,
        String cancellationReason,
        Instant createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getProductCode(),
                order.getAmount(), order.getCurrency(), order.getStatus(), order.getCancellationReason(),
                order.getCreatedAt());
    }
}
