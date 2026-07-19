package com.telcox.paymentservice.domain;

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
 * FR-26: idempotent ödeme. "order_id" üzerindeki unique constraint aynı sipariş için
 * ikinci kez ödeme kaydı oluşturulmasını DB seviyesinde engeller (idempotent consumer, TR-03).
 */
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Payment() {
    }

    public Payment(UUID orderId, BigDecimal amount, String currency, PaymentStatus status, String failureReason) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.failureReason = failureReason;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
