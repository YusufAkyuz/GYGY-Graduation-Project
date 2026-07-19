package com.telcox.billingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Component
public class UsageClient {

    private final RestClient restClient = RestClient.create();
    private final ServiceUrlResolver serviceUrlResolver;

    public UsageClient(ServiceUrlResolver serviceUrlResolver) {
        this.serviceUrlResolver = serviceUrlResolver;
    }

    public BigDecimal getOverageMb(UUID subscriptionId) {
        try {
            String baseUrl = serviceUrlResolver.resolveBaseUrl("usage-service");
            Map<?, ?> body = restClient.get()
                    .uri(baseUrl + "/api/v1/usage/{id}", subscriptionId)
                    .retrieve()
                    .body(Map.class);
            return new BigDecimal(body.get("overageMb").toString());
        } catch (Exception e) {
            return BigDecimal.ZERO; // kullanım kaydı yoksa aşım da yok
        }
    }
}
