package com.telcox.identityservice.service;

import com.telcox.common.error.ApiException;
import com.telcox.identityservice.domain.User;
import com.telcox.identityservice.dto.LoginRequest;
import com.telcox.identityservice.dto.RefreshRequest;
import com.telcox.identityservice.dto.TokenPairResponse;
import com.telcox.identityservice.repository.UserRepository;
import com.telcox.identityservice.security.JwtService;
import com.telcox.identityservice.security.RefreshTokenStore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenStore refreshTokenStore;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        JwtService jwtService, RefreshTokenStore refreshTokenStore) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenStore = refreshTokenStore;
    }

    @Transactional(readOnly = true)
    public TokenPairResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(this::invalidCredentials);

        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw invalidCredentials();
        }

        return issueTokenPair(user);
    }

    public TokenPairResponse refresh(RefreshRequest request) {
        Claims claims;
        try {
            claims = jwtService.parseAndValidate(request.refreshToken());
        } catch (JwtException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid Token", "Refresh token geçersiz veya süresi dolmuş");
        }

        if (!jwtService.isRefreshToken(claims)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid Token", "Sağlanan token bir refresh token değil");
        }

        UUID userId = UUID.fromString(claims.getSubject());
        String presentedJti = claims.getId();
        User user = userRepository.findById(userId).orElseThrow(this::invalidCredentials);

        JwtService.GeneratedRefreshToken newRefresh = jwtService.generateRefreshToken(user);
        boolean rotated = refreshTokenStore.rotateOrRevokeOnReuse(userId, presentedJti, newRefresh.jti());

        if (!rotated) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Token Reuse Detected",
                    "Refresh token yeniden kullanımı tespit edildi; tüm oturumlar iptal edildi");
        }

        String accessToken = jwtService.generateAccessToken(user);
        return new TokenPairResponse(accessToken, newRefresh.token());
    }

    public void logout(RefreshRequest request) {
        try {
            Claims claims = jwtService.parseAndValidate(request.refreshToken());
            UUID userId = UUID.fromString(claims.getSubject());
            refreshTokenStore.revokeAll(userId);
        } catch (JwtException e) {
            // Zaten geçersiz bir token için yapılacak ek bir şey yok.
        }
    }

    private TokenPairResponse issueTokenPair(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        JwtService.GeneratedRefreshToken refresh = jwtService.generateRefreshToken(user);
        refreshTokenStore.store(user.getId(), refresh.jti());
        return new TokenPairResponse(accessToken, refresh.token());
    }

    private ApiException invalidCredentials() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "Invalid Credentials", "Kullanıcı adı veya şifre hatalı");
    }
}
