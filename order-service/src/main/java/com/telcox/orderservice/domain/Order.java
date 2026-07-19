package com.telcox.orderservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Saga'nın merkezi durum kaydı (TR-04). Order, saga koreografisindeki her adımda
 * (OrderCreated -> PaymentCompleted -> SubscriptionActivated) durumunu günceller.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "product_version_id", nullable = false)
    private UUID productVersionId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.DRAFT;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt = Instant.now();

    protected Order() {
    }

    public Order(UUID customerId, String productCode, UUID productVersionId, BigDecimal amount, String currency) {
        this.customerId = customerId;
        this.productCode = productCode;
        this.productVersionId = productVersionId;
        this.amount = amount;
        this.currency = currency;
    }

    public void markPendingPayment() {
        this.status = OrderStatus.PENDING_PAYMENT;
        this.updatedAt = Instant.now();
    }

    public void markPaid() {
        this.status = OrderStatus.PAID;
        this.updatedAt = Instant.now();
    }

    public void markFulfilled() {
        this.status = OrderStatus.FULFILLED;
        this.updatedAt = Instant.now();
    }

    public void cancel(String reason) {
        this.status = OrderStatus.CANCELLED;
        this.cancellationReason = reason;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getProductCode() {
        return productCode;
    }

    public UUID getProductVersionId() {
        return productVersionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
