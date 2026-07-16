package com.telcox.productcatalogservice.service;

import com.telcox.common.error.ApiException;
import com.telcox.common.outbox.OutboxEventPublisherService;
import com.telcox.productcatalogservice.domain.Product;
import com.telcox.productcatalogservice.dto.CreateProductRequest;
import com.telcox.productcatalogservice.event.TariffCreatedEvent;
import com.telcox.productcatalogservice.event.TariffPriceChangedEvent;
import com.telcox.productcatalogservice.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Katalog read-heavy olduğundan (TR-06) güncel versiyon Redis'te cache-aside ile tutulur:
 * okuma önce cache'e bakar, yoksa DB'den çekip cache'ler; versiyon değiştiğinde (yeni fiyat vb.)
 * ilgili "code" cache'ten temizlenir.
 */
@Service
public class ProductCatalogService {

    private static final String AGGREGATE_TYPE = "Product";
    private static final String CACHE_NAME = "products";

    private final ProductRepository productRepository;
    private final OutboxEventPublisherService outboxEventPublisherService;

    public ProductCatalogService(ProductRepository productRepository,
                                  OutboxEventPublisherService outboxEventPublisherService) {
        this.productRepository = productRepository;
        this.outboxEventPublisherService = outboxEventPublisherService;
    }

    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME, key = "#request.code()")
    public Product create(CreateProductRequest request) {
        if (productRepository.existsByCodeAndEffectiveToIsNull(request.code())) {
            throw ApiException.conflict("Bu kodla zaten güncel bir ürün var: " + request.code());
        }

        Product product = new Product(request.code(), request.name(), request.description(),
                request.productType(), request.subscriberClass(), request.monthlyPrice(),
                request.currency() == null ? "TRY" : request.currency(),
                request.targetSegment(), 1, null, Instant.now());

        product = productRepository.save(product);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, product.getId().toString(),
                "TariffCreated", new TariffCreatedEvent(product.getId(), product.getCode(), product.getMonthlyPrice()));

        return product;
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#code")
    @Transactional(readOnly = true)
    public Product getCurrentByCode(String code) {
        return productRepository.findByCodeAndEffectiveToIsNull(code)
                .orElseThrow(() -> ApiException.notFound("Ürün bulunamadı: " + code));
    }

    @Transactional(readOnly = true)
    public Product getVersionById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Ürün versiyonu bulunamadı: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Product> list(Pageable pageable) {
        return productRepository.findAllByEffectiveToIsNull(pageable);
    }

    /**
     * FR-08: fiyat değişikliği mevcut versiyonu kapatıp yeni bir versiyon açar; eski versiyonun
     * id'sini referans alan mevcut abonelikler etkilenmez, sadece yeni siparişler yeni fiyatı görür.
     */
    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME, key = "#code")
    public Product changePrice(String code, BigDecimal newPrice) {
        Product current = productRepository.findByCodeAndEffectiveToIsNull(code)
                .orElseThrow(() -> ApiException.notFound("Ürün bulunamadı: " + code));

        Instant now = Instant.now();
        current.closeVersion(now);
        productRepository.saveAndFlush(current);

        Product newVersion = new Product(current.getCode(), current.getName(), current.getDescription(),
                current.getProductType(), current.getSubscriberClass(), newPrice, current.getCurrency(),
                current.getTargetSegment(), current.getVersionNumber() + 1, current.getId(), now);
        newVersion = productRepository.save(newVersion);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, newVersion.getId().toString(),
                "TariffPriceChanged", new TariffPriceChangedEvent(code, current.getId(), newVersion.getId(),
                        current.getMonthlyPrice(), newPrice));

        return newVersion;
    }

    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME, key = "#code")
    public void deactivate(String code) {
        Product current = productRepository.findByCodeAndEffectiveToIsNull(code)
                .orElseThrow(() -> ApiException.notFound("Ürün bulunamadı: " + code));
        current.closeVersion(Instant.now());
        productRepository.save(current);
    }
}
