package com.telcox.common.security.pii;

import javax.crypto.SecretKey;

/**
 * JPA AttributeConverter'lar Hibernate tarafından reflection ile örneklenebildiğinden
 * (her zaman Spring bean container'a bağlı olmayabilir), şifreleme anahtarını statik
 * olarak tutar. PiiEncryptionAutoConfiguration uygulama başlarken bunu bir kez set eder.
 */
public final class PiiKeyHolder {

    private static volatile SecretKey key;

    private PiiKeyHolder() {
    }

    public static void init(String base64Key) {
        key = AesGcmCodec.keyFromBase64(base64Key);
    }

    public static SecretKey getKey() {
        if (key == null) {
            throw new IllegalStateException(
                    "PII şifreleme anahtarı henüz başlatılmadı: telcox.security.pii-encryption-key config'i eksik olabilir");
        }
        return key;
    }
}
