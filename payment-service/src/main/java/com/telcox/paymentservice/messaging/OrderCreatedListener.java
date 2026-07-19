package com.telcox.paymentservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telcox.paymentservice.event.OrderCreatedEvent;
import com.telcox.paymentservice.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public OrderCreatedListener(PaymentService paymentService, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "telcox.OrderCreated", groupId = "payment-service")
    public void onOrderCreated(String payload) throws Exception {
        OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
        paymentService.processPayment(event);
    }
}
