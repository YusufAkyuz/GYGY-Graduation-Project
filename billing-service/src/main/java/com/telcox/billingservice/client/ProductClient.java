package com.telcox.billingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class ProductClient {

    private final RestClient restClient = RestClient.create();
    private final ServiceUrlResolver serviceUrlResolver;

    public ProductClient(ServiceUrlResolver serviceUrlResolver) {
        this.serviceUrlResolver = serviceUrlResolver;
    }

    public BigDecimal getMonthlyPrice(String productCode) {
        String baseUrl = serviceUrlResolver.resolveBaseUrl("product-catalog-service");
        Map<?, ?> body = restClient.get()
                .uri(baseUrl + "/api/v1/products/{code}", productCode)
                .retrieve()
                .body(Map.class);
        return new BigDecimal(body.get("monthlyPrice").toString());
    }
}
