package com.telcox.identityservice.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Gateway'in enjekte ettiği X-User-Id/X-User-Roles header'larından kurulan
 * SecurityContext'i doğrulamak için basit bir uç (SR-02, SR-04, Milestone M1).
 */
@RestController
public class MeController {

    @GetMapping("/api/v1/users/me")
    public MeResponse me(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new MeResponse(authentication.getName(), roles);
    }

    public record MeResponse(String userId, List<String> roles) {
    }
}
