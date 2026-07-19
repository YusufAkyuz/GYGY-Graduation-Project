package com.telcox.billingservice.domain;

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
import java.time.YearMonth;
import java.util.UUID;

/** FR-21..FR-24: bill-run, fatura kalemleri (aylık ücret + aşım + vergi), ödeme sonrası PAID. */
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "subscription_id", nullable = false)
    private UUID subscriptionId;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "billing_period", nullable = false)
    private String billingPeriod;

    @Column(name = "monthly_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyFee;

    @Column(name = "overage_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal overageFee;

    @Column(name = "tax_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status = InvoiceStatus.ISSUED;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Invoice() {
    }

    public Invoice(UUID subscriptionId, UUID customerId, YearMonth billingPeriod, BigDecimal monthlyFee,
                    BigDecimal overageFee, BigDecimal taxAmount, BigDecimal totalAmount) {
        this.subscriptionId = subscriptionId;
        this.customerId = customerId;
        this.billingPeriod = billingPeriod.toString();
        this.monthlyFee = monthlyFee;
        this.overageFee = overageFee;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
    }

    public void markPaid() {
        this.status = InvoiceStatus.PAID;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public BigDecimal getOverageFee() {
        return overageFee;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
