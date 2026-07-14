package com.telcox.apigateway.filter;

import com.telcox.apigateway.config.RateLimitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

/**
 * Redis tabanlı sabit-pencere (fixed-window) rate limiter: varsayılan 100 istek/dakika/kullanıcı (SR-07).
 * Anahtar, JwtAuthenticationGlobalFilter'ın enjekte ettiği X-User-Id header'ından; kimliksiz
 * istekler (ör. login) için istemci IP'sinden türetilir.
 */
@Component
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitingGlobalFilter implements GlobalFilter, Ordered {

    private static final String KEY_PREFIX = "ratelimit:";

    private final ReactiveStringRedisTemplate redisTemplate;
    private final int requestsPerMinute;

    public RateLimitingGlobalFilter(ReactiveStringRedisTemplate redisTemplate, RateLimitProperties properties) {
        this.redisTemplate = redisTemplate;
        this.requestsPerMinute = properties.getRequestsPerMinute();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String rateLimitKey = KEY_PREFIX + resolveKey(exchange.getRequest()) + ":" + currentMinuteBucket();

        return redisTemplate.opsForValue().increment(rateLimitKey)
                .flatMap(count -> {
                    if (count == 1L) {
                        return redisTemplate.expire(rateLimitKey, Duration.ofSeconds(60)).thenReturn(count);
                    }
                    return Mono.just(count);
                })
                .flatMap(count -> {
                    if (count > requestsPerMinute) {
                        return tooManyRequests(exchange);
                    }
                    return chain.filter(exchange);
                });
    }

    private String resolveKey(ServerHttpRequest request) {
        String userId = request.getHeaders().getFirst(JwtAuthenticationGlobalFilter.USER_ID_HEADER);
        if (StringUtils.hasText(userId)) {
            return "user:" + userId;
        }
        String remoteAddress = request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : "unknown";
        return "ip:" + remoteAddress;
    }

    private long currentMinuteBucket() {
        return Instant.now().getEpochSecond() / 60;
    }

    private Mono<Void> tooManyRequests(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = """
                {"type":"about:blank","title":"Too Many Requests","status":429,"detail":"Dakikalık istek limiti aşıldı"}""";
        var buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
