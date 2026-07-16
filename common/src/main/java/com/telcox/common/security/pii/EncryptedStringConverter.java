package com.telcox.common.security.pii;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Entity alanlarında @Convert(converter = EncryptedStringConverter.class) ile kullanılır
 * (SR-05: TCKN, kart no gibi PII alanlarının AES-GCM ile şifrelenmesi).
 */
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : AesGcmCodec.encrypt(attribute, PiiKeyHolder.getKey());
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AesGcmCodec.decrypt(dbData, PiiKeyHolder.getKey());
    }
}
