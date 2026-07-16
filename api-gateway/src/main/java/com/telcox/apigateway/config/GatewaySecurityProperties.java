package com.telcox.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "telcox.gateway")
public class GatewaySecurityProperties {

    /** JWT doğrulaması yapılmadan geçilecek path (+ opsiyonel method) kuralları (SR-02). */
    private List<PublicPathRule> publicPaths = List.of();

    public List<PublicPathRule> getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(List<PublicPathRule> publicPaths) {
        this.publicPaths = publicPaths;
    }
}
