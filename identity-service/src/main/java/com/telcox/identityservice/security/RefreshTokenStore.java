package com.telcox.identityservice.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

/**
 * Kullanıcı başına aktif refresh-token jti'lerini Redis Set'te tutar (SR-03).
 * - login: jti set'e eklenir.
 * - refresh: jti set'te varsa rotate edilir (silinir + yenisi eklenir).
 * - reuse tespiti: jti set'te yoksa (daha önce rotate edilmiş/iptal edilmiş) tüm set silinir,
 *   yani kullanıcının tüm aktif oturumları iptal edilir.
 */
@Component
public class RefreshTokenStore {

    private static final String KEY_PREFIX = "refresh-tokens:";

    private final StringRedisTemplate redisTemplate;
    private final JwtService jwtService;

    public RefreshTokenStore(StringRedisTemplate redisTemplate, JwtService jwtService) {
        this.redisTemplate = redisTemplate;
        this.jwtService = jwtService;
    }

    public void store(UUID userId, String jti) {
        String key = key(userId);
        redisTemplate.opsForSet().add(key, jti);
        redisTemplate.expire(key, Duration.ofSeconds(jwtService.getRefreshTokenTtlSeconds()));
    }

    /** @return true ise jti geçerliydi ve rotate edildi; false ise reuse tespit edildi ve tüm oturumlar iptal edildi. */
    public boolean rotateOrRevokeOnReuse(UUID userId, String presentedJti, String newJti) {
        String key = key(userId);
        Long removed = redisTemplate.opsForSet().remove(key, presentedJti);
        if (removed == null || removed == 0) {
            redisTemplate.delete(key);
            return false;
        }
        redisTemplate.opsForSet().add(key, newJti);
        redisTemplate.expire(key, Duration.ofSeconds(jwtService.getRefreshTokenTtlSeconds()));
        return true;
    }

    public void revokeAll(UUID userId) {
        redisTemplate.delete(key(userId));
    }

    private String key(UUID userId) {
        return KEY_PREFIX + userId;
    }
}
