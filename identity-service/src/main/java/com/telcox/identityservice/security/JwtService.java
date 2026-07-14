package com.telcox.identityservice.security;

import com.telcox.identityservice.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Access + refresh JWT üretimi ve doğrulaması (SR-01). Access token'lar stateless
 * doğrulanır; refresh token'ların oturum durumu Redis'te (RefreshTokenStore) tutulur.
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtService {

    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    private final JwtProperties properties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.signingKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(properties.getSecret()));
    }

    public String generateAccessToken(User user) {
        List<String> roles = user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim(CLAIM_ROLES, roles)
                .claim(CLAIM_TYPE, TYPE_ACCESS)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(properties.getAccessTokenTtlSeconds(), ChronoUnit.SECONDS)))
                .signWith(signingKey)
                .compact();
    }

    /** jti üretir ve token ile birlikte döner; çağıran taraf jti'yi Redis'e yazar (rotation). */
    public GeneratedRefreshToken generateRefreshToken(User user) {
        String jti = UUID.randomUUID().toString();
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(user.getId().toString())
                .id(jti)
                .claim(CLAIM_TYPE, TYPE_REFRESH)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(properties.getRefreshTokenTtlSeconds(), ChronoUnit.SECONDS)))
                .signWith(signingKey)
                .compact();
        return new GeneratedRefreshToken(token, jti);
    }

    public Claims parseAndValidate(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isRefreshToken(Claims claims) {
        return TYPE_REFRESH.equals(claims.get(CLAIM_TYPE, String.class));
    }

    public long getRefreshTokenTtlSeconds() {
        return properties.getRefreshTokenTtlSeconds();
    }

    public record GeneratedRefreshToken(String token, String jti) {
    }
}
