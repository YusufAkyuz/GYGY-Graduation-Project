package com.telcox.productcatalogservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Ürün kataloğu satırı; her satır tek bir VERSİYONU temsil eder (FR-08: tarife değişiklikleri
 * versiyonlanır, mevcut abonelerin tarifesi korunur — SCD Type 2 benzeri model).
 * "code" iş kimliğidir ve versiyonlar arasında sabit kalır; "id" her versiyona özeldir ve
 * subscription-service gibi tüketiciler abonelik anındaki tam fiyat/koşulu bu id ile referanslar.
 */
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscriber_class", nullable = false)
    private SubscriberClass subscriberClass;

    @Column(name = "monthly_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyPrice;

    @Column(nullable = false, length = 3)
    private String currency = "TRY";

    @Column(name = "target_segment")
    private String targetSegment;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "previous_version_id")
    private UUID previousVersionId;

    @Column(name = "effective_from", nullable = false)
    private Instant effectiveFrom;

    /** null ise bu versiyon hâlâ yürürlükte (güncel). */
    @Column(name = "effective_to")
    private Instant effectiveTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Product() {
    }

    public Product(String code, String name, String description, ProductType productType,
                   SubscriberClass subscriberClass, BigDecimal monthlyPrice, String currency,
                   String targetSegment, int versionNumber, UUID previousVersionId, Instant effectiveFrom) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.productType = productType;
        this.subscriberClass = subscriberClass;
        this.monthlyPrice = monthlyPrice;
        this.currency = currency;
        this.targetSegment = targetSegment;
        this.versionNumber = versionNumber;
        this.previousVersionId = previousVersionId;
        this.effectiveFrom = effectiveFrom;
    }

    public void closeVersion(Instant effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public boolean isCurrent() {
        return effectiveTo == null;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProductType getProductType() {
        return productType;
    }

    public SubscriberClass getSubscriberClass() {
        return subscriberClass;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTargetSegment() {
        return targetSegment;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public UUID getPreviousVersionId() {
        return previousVersionId;
    }

    public Instant getEffectiveFrom() {
        return effectiveFrom;
    }

    public Instant getEffectiveTo() {
        return effectiveTo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
