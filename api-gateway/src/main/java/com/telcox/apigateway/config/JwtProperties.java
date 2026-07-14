package com.telcox.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telcox.jwt")
public class JwtProperties {

    /** Base64 encoded HMAC-SHA256 secret; identity-service ile aynı olmalı (SR-01, SR-02). */
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
