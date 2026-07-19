package com.telcox.orderservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telcox.orderservice.event.PaymentCompletedEvent;
import com.telcox.orderservice.event.PaymentFailedEvent;
import com.telcox.orderservice.event.SubscriptionActivatedEvent;
import com.telcox.orderservice.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Saga koreografisinin order-service tarafı: payment-service ve subscription-service'in
 * yayınladığı event'leri dinleyip sipariş durumunu ilerletir (TR-04).
 * Kafka consumer group ("order-service") sayesinde her event yalnızca bir kez işlenir.
 */
@Component
public class SagaEventListener {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public SagaEventListener(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "telcox.PaymentCompleted", groupId = "order-service")
    public void onPaymentCompleted(String payload) throws Exception {
        PaymentCompletedEvent event = objectMapper.readValue(payload, PaymentCompletedEvent.class);
        orderService.markPaid(event.orderId());
    }

    @KafkaListener(topics = "telcox.PaymentFailed", groupId = "order-service")
    public void onPaymentFailed(String payload) throws Exception {
        PaymentFailedEvent event = objectMapper.readValue(payload, PaymentFailedEvent.class);
        orderService.cancel(event.orderId(), "Ödeme başarısız: " + event.reason());
    }

    @KafkaListener(topics = "telcox.SubscriptionActivated", groupId = "order-service")
    public void onSubscriptionActivated(String payload) throws Exception {
        SubscriptionActivatedEvent event = objectMapper.readValue(payload, SubscriptionActivatedEvent.class);
        orderService.markFulfilled(event.orderId());
    }
}
