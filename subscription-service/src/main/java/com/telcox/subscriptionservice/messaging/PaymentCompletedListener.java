package com.telcox.subscriptionservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telcox.subscriptionservice.event.PaymentCompletedEvent;
import com.telcox.subscriptionservice.service.SubscriptionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedListener {

    private final SubscriptionService subscriptionService;
    private final ObjectMapper objectMapper;

    public PaymentCompletedListener(SubscriptionService subscriptionService, ObjectMapper objectMapper) {
        this.subscriptionService = subscriptionService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "telcox.PaymentCompleted", groupId = "subscription-service")
    public void onPaymentCompleted(String payload) throws Exception {
        PaymentCompletedEvent event = objectMapper.readValue(payload, PaymentCompletedEvent.class);
        subscriptionService.activateSubscription(event);
    }
}
