package com.telcox.orderservice.client;

import com.telcox.common.error.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Senkron katalog fiyat snapshot'ı (TR-05): sipariş anındaki güncel fiyat sipariş üzerine kilitlenir. */
@Component
public class ProductClient {

    private final RestClient restClient = RestClient.create();
    private final ServiceUrlResolver serviceUrlResolver;

    public ProductClient(ServiceUrlResolver serviceUrlResolver) {
        this.serviceUrlResolver = serviceUrlResolver;
    }

    public ProductClientResponse getCurrentProduct(String code) {
        try {
            String baseUrl = serviceUrlResolver.resolveBaseUrl("product-catalog-service");
            return restClient.get()
                    .uri(baseUrl + "/api/v1/products/{code}", code)
                    .retrieve()
                    .body(ProductClientResponse.class);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Catalog Service Unavailable",
                    "Ürün bilgisi doğrulanamadı: " + e.getMessage());
        }
    }
}
