package com.telcox.common.error;

import java.time.Instant;
import java.util.List;

/**
 * RFC 7807 Problem Details gövdesi. TR-17 gereksinimine göre tüm servislerde
 * hata yanıtları bu formatta döner.
 */
public record ErrorResponse(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String correlationId,
        Instant timestamp,
        List<FieldError> errors
) {

    public record FieldError(String field, String message) {
    }

    public static ErrorResponse of(String title, int status, String detail, String instance, String correlationId) {
        return new ErrorResponse("about:blank", title, status, detail, instance, correlationId, Instant.now(), List.of());
    }

    public static ErrorResponse of(String title, int status, String detail, String instance, String correlationId,
                                    List<FieldError> errors) {
        return new ErrorResponse("about:blank", title, status, detail, instance, correlationId, Instant.now(), errors);
    }
}
