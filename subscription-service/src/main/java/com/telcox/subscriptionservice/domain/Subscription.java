package com.telcox.subscriptionservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/** FR-13..FR-15: sipariş tamamlanınca otomatik aktivasyon; bir müşterinin birden fazla aboneliği olabilir. */
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(nullable = false, unique = true)
    private String msisdn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Column(name = "activated_at", nullable = false)
    private Instant activatedAt = Instant.now();

    protected Subscription() {
    }

    public Subscription(UUID customerId, UUID orderId, String productCode, String msisdn) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.productCode = productCode;
        this.msisdn = msisdn;
    }

    public void suspend() {
        this.status = SubscriptionStatus.SUSPENDED;
    }

    public void reactivate() {
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void terminate() {
        this.status = SubscriptionStatus.TERMINATED;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public Instant getActivatedAt() {
        return activatedAt;
    }
}
