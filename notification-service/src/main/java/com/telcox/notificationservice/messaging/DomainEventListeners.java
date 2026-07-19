package com.telcox.notificationservice.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telcox.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Domain event'lerine template tabanlı abonelik: welcome SMS, fatura e-postası, kota uyarıları,
 * ticket bildirimleri (FR-28..FR-33 arası bildirim gereksinimleri).
 */
@Component
public class DomainEventListeners {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public DomainEventListeners(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "telcox.SubscriptionActivated", groupId = "notification-service")
    public void onSubscriptionActivated(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        notificationService.send("WELCOME_SMS", node.get("msisdn") != null ? node.get("msisdn").asText() : node.get("orderId").asText(),
                Map.of("msisdn", node.path("msisdn").asText("")));
    }

    @KafkaListener(topics = "telcox.InvoiceGenerated", groupId = "notification-service")
    public void onInvoiceGenerated(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        notificationService.send("INVOICE_EMAIL", node.get("customerId").asText(),
                Map.of("amount", node.path("totalAmount").asText(""), "period", node.path("billingPeriod").asText("")));
    }

    @KafkaListener(topics = "telcox.QuotaThresholdReached", groupId = "notification-service")
    public void onQuotaThreshold(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        notificationService.send("QUOTA_WARNING_SMS", node.path("msisdn").asText(),
                Map.of("percent", node.path("thresholdPercent").asText("80")));
    }

    @KafkaListener(topics = "telcox.QuotaExceeded", groupId = "notification-service")
    public void onQuotaExceeded(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        notificationService.send("QUOTA_EXCEEDED_SMS", node.path("msisdn").asText(),
                Map.of("overageMb", node.path("overageMb").asText("0")));
    }

    @KafkaListener(topics = "telcox.TicketOpened", groupId = "notification-service")
    public void onTicketOpened(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        notificationService.send("TICKET_OPENED_SMS", node.path("customerId").asText(),
                Map.of("subject", node.path("subject").asText("")));
    }

    @KafkaListener(topics = "telcox.CustomerRegistered", groupId = "notification-service")
    public void onCustomerRegistered(String payload) throws Exception {
        JsonNode node = objectMapper.readTree(payload);
        notificationService.send("CUSTOMER_REGISTERED_EMAIL", node.path("email").asText(), Map.of());
    }
}
