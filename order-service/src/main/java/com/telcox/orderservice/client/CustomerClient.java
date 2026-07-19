package com.telcox.orderservice.client;

import com.telcox.common.error.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

/** Senkron müşteri doğrulaması (TR-05): sipariş sırasında müşterinin ACTIVE olduğu kontrol edilir. */
@Component
public class CustomerClient {

    private final RestClient restClient = RestClient.create();
    private final ServiceUrlResolver serviceUrlResolver;

    public CustomerClient(ServiceUrlResolver serviceUrlResolver) {
        this.serviceUrlResolver = serviceUrlResolver;
    }

    public CustomerClientResponse getActiveCustomer(UUID customerId) {
        try {
            String baseUrl = serviceUrlResolver.resolveBaseUrl("customer-service");
            return restClient.get()
                    .uri(baseUrl + "/api/v1/customers/{id}", customerId)
                    .header("X-User-Roles", "ROLE_ADMIN")
                    .header("X-User-Id", "order-service")
                    .retrieve()
                    .body(CustomerClientResponse.class);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Customer Service Unavailable",
                    "Müşteri bilgisi doğrulanamadı: " + e.getMessage());
        }
    }
}
