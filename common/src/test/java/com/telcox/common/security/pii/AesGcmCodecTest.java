package com.telcox.common.security.pii;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;

class AesGcmCodecTest {

    @Test
    void encryptThenDecryptReturnsOriginalValue() {
        byte[] rawKey = new byte[32];
        new SecureRandom().nextBytes(rawKey);
        SecretKey key = AesGcmCodec.keyFromBase64(Base64.getEncoder().encodeToString(rawKey));

        String plaintext = "10000000146";
        String encrypted = AesGcmCodec.encrypt(plaintext, key);

        assertThat(encrypted).isNotEqualTo(plaintext);
        assertThat(AesGcmCodec.decrypt(encrypted, key)).isEqualTo(plaintext);
    }

    @Test
    void sameInputEncryptsDifferentlyEachTime() {
        byte[] rawKey = new byte[32];
        new SecureRandom().nextBytes(rawKey);
        SecretKey key = AesGcmCodec.keyFromBase64(Base64.getEncoder().encodeToString(rawKey));

        String a = AesGcmCodec.encrypt("value", key);
        String b = AesGcmCodec.encrypt("value", key);

        assertThat(a).isNotEqualTo(b); // rastgele IV nedeniyle
    }
}
