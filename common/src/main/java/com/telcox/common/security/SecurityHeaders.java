package com.telcox.common.security;

/**
 * Gateway'in JWT'den türetip downstream servislere ilettiği trust-boundary header'ları (SR-02).
 * Gateway arkasında (mTLS olmadan) çalışıldığı için servisler bu header'lara güvenir;
 * bu yüzden gateway dışarıdan gelen aynı isimli header'ları daima ezmelidir.
 */
public final class SecurityHeaders {

    public static final String USER_ID = "X-User-Id";
    public static final String USER_ROLES = "X-User-Roles";
    public static final String ROLES_DELIMITER = ",";

    private SecurityHeaders() {
    }
}
