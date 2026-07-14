package com.telcox.apigateway.filter;

import com.telcox.apigateway.config.GatewaySecurityProperties;
import com.telcox.apigateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gateway'de JWT doğrulaması: erişim token'ının imzasını ve süresini kontrol eder,
 * kullanıcı kimliği/rollerini X-User-Id / X-User-Roles header'larına yazar (SR-02).
 * Downstream servisler bu header'lara güvenir; bu yüzden istemciden gelen aynı isimli
 * header'lar burada daima temizlenir (spoofing önlemi).
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, GatewaySecurityProperties.class})
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String USER_ROLES_HEADER = "X-User-Roles";

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final SecretKey signingKey;
    private final List<String> publicPaths;

    public JwtAuthenticationGlobalFilter(JwtProperties jwtProperties, GatewaySecurityProperties securityProperties) {
        this.signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getSecret()));
        this.publicPaths = securityProperties.getPublicPaths();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        ServerHttpRequest.Builder mutatedRequestBuilder = request.mutate()
                .headers(headers -> {
                    headers.remove(USER_ID_HEADER);
                    headers.remove(USER_ROLES_HEADER);
                });

        if (isPublicPath(path)) {
            return chain.filter(exchange.mutate().request(mutatedRequestBuilder.build()).build());
        }

        String authorizationHeader = request.getHeaders().getFirst("Authorization");
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Authorization header eksik veya hatalı biçimli");
        }

        String token = authorizationHeader.substring("Bearer ".length());
        Claims claims;
        try {
            claims = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return unauthorized(exchange, "Token geçersiz veya süresi dolmuş");
        }

        if (!"access".equals(claims.get("type", String.class))) {
            return unauthorized(exchange, "Sağlanan token bir erişim token'ı değil");
        }

        String userId = claims.getSubject();
        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);
        String rolesHeaderValue = roles == null ? "" : roles.stream().collect(Collectors.joining(","));

        ServerHttpRequest mutatedRequest = mutatedRequestBuilder
                .header(USER_ID_HEADER, userId)
                .header(USER_ROLES_HEADER, rolesHeaderValue)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublicPath(String path) {
        return publicPaths.stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String detail) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = """
                {"type":"about:blank","title":"Unauthorized","status":401,"detail":"%s"}""".formatted(detail);
        var buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
