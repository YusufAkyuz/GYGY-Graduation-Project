package com.telcox.billingservice.client;

import com.telcox.common.error.ApiException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return instances.get(0).getUri().toString();
    }
}
