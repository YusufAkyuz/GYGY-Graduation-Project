package com.telcox.identityservice.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telcox.jwt")
public class JwtProperties {

    /** Base64 encoded HMAC-SHA256 secret; gateway ile aynı olmalı (SR-01, SR-02). */
    private String secret;
    private long accessTokenTtlSeconds = 900;       // 15 dk
    private long refreshTokenTtlSeconds = 604800;   // 7 gün

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getAccessTokenTtlSeconds() {
        return accessTokenTtlSeconds;
    }

    public void setAccessTokenTtlSeconds(long accessTokenTtlSeconds) {
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
    }

    public long getRefreshTokenTtlSeconds() {
        return refreshTokenTtlSeconds;
    }

    public void setRefreshTokenTtlSeconds(long refreshTokenTtlSeconds) {
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
    }
}
