package com.telcox.billingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class SubscriptionClient {

    private final RestClient restClient = RestClient.create();
    private final ServiceUrlResolver serviceUrlResolver;

    public SubscriptionClient(ServiceUrlResolver serviceUrlResolver) {
        this.serviceUrlResolver = serviceUrlResolver;
    }

    @SuppressWarnings("unchecked")
    public List<SubscriptionClientResponse> listAllActive() {
        String baseUrl = serviceUrlResolver.resolveBaseUrl("subscription-service");
        Map<String, Object> page = restClient.get()
                .uri(baseUrl + "/api/v1/subscriptions/active?size=1000")
                .header("X-User-Roles", "ROLE_ADMIN")
                .header("X-User-Id", "billing-service")
                .retrieve()
                .body(Map.class);
        List<Map<String, Object>> content = (List<Map<String, Object>>) page.get("content");
        return content.stream()
                .map(m -> new SubscriptionClientResponse(
                        java.util.UUID.fromString((String) m.get("id")),
                        java.util.UUID.fromString((String) m.get("customerId")),
                        (String) m.get("productCode"),
                        (String) m.get("msisdn")))
                .toList();
    }
}
