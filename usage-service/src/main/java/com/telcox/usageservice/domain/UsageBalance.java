package com.telcox.usageservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/** FR-17..FR-20: kalan kota, %80/%100 eşikleri, aşım agregasyonu. MVP'de tek metrik: veri (MB). */
@Entity
@Table(name = "usage_balances")
public class UsageBalance {

    @Id
    @Column(name = "subscription_id")
    private UUID subscriptionId;

    @Column(nullable = false)
    private String msisdn;

    @Column(name = "quota_mb", nullable = false, precision = 12, scale = 2)
    private BigDecimal quotaMb;

    @Column(name = "used_mb", nullable = false, precision = 12, scale = 2)
    private BigDecimal usedMb = BigDecimal.ZERO;

    @Column(name = "overage_mb", nullable = false, precision = 12, scale = 2)
    private BigDecimal overageMb = BigDecimal.ZERO;

    @Column(name = "threshold_80_notified", nullable = false)
    private boolean threshold80Notified = false;

    @Column(name = "threshold_100_notified", nullable = false)
    private boolean threshold100Notified = false;

    protected UsageBalance() {
    }

    public UsageBalance(UUID subscriptionId, String msisdn, BigDecimal quotaMb) {
        this.subscriptionId = subscriptionId;
        this.msisdn = msisdn;
        this.quotaMb = quotaMb;
    }

    public void recordUsage(BigDecimal dataMb) {
        BigDecimal remaining = quotaMb.subtract(usedMb);
        if (dataMb.compareTo(remaining) > 0) {
            overageMb = overageMb.add(dataMb.subtract(remaining.max(BigDecimal.ZERO)));
        }
        usedMb = usedMb.add(dataMb);
    }

    public BigDecimal usagePercentage() {
        if (quotaMb.signum() == 0) {
            return BigDecimal.ZERO;
        }
        return usedMb.divide(quotaMb, 4, java.math.RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public boolean crossed80() {
        return usagePercentage().compareTo(BigDecimal.valueOf(80)) >= 0;
    }

    public boolean crossed100() {
        return usagePercentage().compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    public void markThreshold80Notified() {
        this.threshold80Notified = true;
    }

    public void markThreshold100Notified() {
        this.threshold100Notified = true;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public BigDecimal getQuotaMb() {
        return quotaMb;
    }

    public BigDecimal getUsedMb() {
        return usedMb;
    }

    public BigDecimal getOverageMb() {
        return overageMb;
    }

    public boolean isThreshold80Notified() {
        return threshold80Notified;
    }

    public boolean isThreshold100Notified() {
        return threshold100Notified;
    }
}
