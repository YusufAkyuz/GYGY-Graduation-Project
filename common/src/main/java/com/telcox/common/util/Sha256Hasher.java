package com.telcox.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * PII alanlarını (ör. TCKN) şifrelenmiş olarak saklarken deterministik arama/uniqueness
 * için kullanılan tek yönlü hash. Şifreleme (AES-GCM) rastgele IV kullandığından
 * benzersizlik kontrolü şifreli değer üzerinden yapılamaz; bu yüzden ayrı bir hash sütunu gerekir.
 */
public final class Sha256Hasher {

    private Sha256Hasher() {
    }

    public static String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algoritması bulunamadı", e);
        }
    }
}
