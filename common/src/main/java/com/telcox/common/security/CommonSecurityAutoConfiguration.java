package com.telcox.common.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.web.SecurityFilterChain;

/**
 * TrustedHeaderAuthenticationFilter'ı bilerek global bir Servlet filtresi olarak
 * KAYDETMEZ: Spring Security'nin kendi SecurityContextHolderFilter'ı, zincirin başında
 * context'i sıfırlar ve zincir dışında erken çalışan bir filtrenin koyduğu Authentication'ı
 * ezer. Bunun yerine her servis kendi SecurityConfig'inde
 * http.addFilterBefore(new TrustedHeaderAuthenticationFilter(), AnonymousAuthenticationFilter.class)
 * ile zincirin içine eklemelidir.
 */
@AutoConfiguration
@ConditionalOnClass(SecurityFilterChain.class)
public class CommonSecurityAutoConfiguration {
}
