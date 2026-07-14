package com.telcox.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "telcox.gateway")
public class GatewaySecurityProperties {

    /** JWT doğrulaması yapılmadan geçilecek Ant-style path'ler (SR-02). */
    private List<String> publicPaths = List.of();

    public List<String> getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
    }
}
