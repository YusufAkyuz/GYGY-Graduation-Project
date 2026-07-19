package com.telcox.subscriptionservice.service;

import com.telcox.common.error.ApiException;
import com.telcox.common.outbox.OutboxEventPublisherService;
import com.telcox.subscriptionservice.domain.Subscription;
import com.telcox.subscriptionservice.domain.SubscriptionStatus;
import com.telcox.subscriptionservice.event.PaymentCompletedEvent;
import com.telcox.subscriptionservice.event.SubscriptionActivatedEvent;
import com.telcox.subscriptionservice.repository.MsisdnPoolRepository;
import com.telcox.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** FR-13: sipariş tamamlanınca (PaymentCompleted) aboneliğin otomatik aktivasyonu ve MSISDN ataması. */
@Service
public class SubscriptionService {

    private static final String AGGREGATE_TYPE = "Subscription";

    private final SubscriptionRepository subscriptionRepository;
    private final MsisdnPoolRepository msisdnPoolRepository;
    private final OutboxEventPublisherService outboxEventPublisherService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, MsisdnPoolRepository msisdnPoolRepository,
                                OutboxEventPublisherService outboxEventPublisherService) {
        this.subscriptionRepository = subscriptionRepository;
        this.msisdnPoolRepository = msisdnPoolRepository;
        this.outboxEventPublisherService = outboxEventPublisherService;
    }

    @Transactional
    public void activateSubscription(PaymentCompletedEvent event) {
        if (subscriptionRepository.existsByOrderId(event.orderId())) {
            return; // idempotent: bu sipariş için abonelik zaten aktive edilmiş
        }

        String msisdn = msisdnPoolRepository.lockNextAvailable()
                .orElseThrow(() -> new IllegalStateException("MSISDN havuzunda kullanılabilir numara kalmadı"));
        msisdnPoolRepository.markAllocated(msisdn);

        Subscription subscription = new Subscription(event.customerId(), event.orderId(), event.productCode(), msisdn);
        subscription = subscriptionRepository.save(subscription);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, subscription.getId().toString(), "SubscriptionActivated",
                new SubscriptionActivatedEvent(event.orderId(), subscription.getId(), msisdn));
    }

    @Transactional(readOnly = true)
    public Subscription getByOrderId(java.util.UUID orderId) {
        return subscriptionRepository.findByOrderId(orderId)
                .orElseThrow(() -> ApiException.notFound("Bu sipariş için abonelik bulunamadı: " + orderId));
    }

    @Transactional(readOnly = true)
    public Page<Subscription> listActive(Pageable pageable) {
        return subscriptionRepository.findAllByStatus(SubscriptionStatus.ACTIVE, pageable);
    }
}
