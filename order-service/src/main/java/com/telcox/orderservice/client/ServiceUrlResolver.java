package com.telcox.orderservice.client;

import com.telcox.common.error.ApiException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Eureka'dan basit round-robin olmadan (MVP için ilk sağlıklı örnek yeterli) servis
 * adresini çözer. Genel amaçlı @LoadBalanced RestClient.Builder bean'i, Eureka istemcisinin
 * kendi dahili HTTP client'ıyla çakıştığından (herhangi bir başka bean tarafından yanlışlıkla
 * enjekte edilebiliyor) bilinçli olarak kullanılmıyor.
 */
@Component
public class ServiceUrlResolver {

    private final DiscoveryClient discoveryClient;

    public ServiceUrlResolver(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String resolveBaseUrl(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (instances.isEmpty()) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable",
                    serviceId + " için Eureka'da sağlıklı örnek bulunamadı");
        }
        ServiceInstance instance = instances.get(0);
        return instance.getUri().toString();
    }
}
