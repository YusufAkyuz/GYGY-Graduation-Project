package com.telcox.common.error;

import com.telcox.common.web.CorrelationIdHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Servislerin extend edip @RestControllerAdvice ile tekrar açıklayabileceği ortak
 * hata çevirme mantığı. Doğrudan @RestControllerAdvice olarak işaretlenmemiştir ki
 * her servis kendi ek handler'larını üstüne ekleyebilsin.
 */
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.of(
                ex.getTitle(),
                ex.getStatus().value(),
                ex.getMessage(),
                request.getRequestURI(),
                CorrelationIdHolder.get()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldError(fe.getField(), describe(fe)))
                .toList();
        ErrorResponse body = ErrorResponse.of(
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                "Bir veya daha fazla alan geçersiz",
                request.getRequestURI(),
                CorrelationIdHolder.get(),
                fieldErrors
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.of(
                "Forbidden",
                HttpStatus.FORBIDDEN.value(),
                "Bu işlem için yetkiniz yok",
                request.getRequestURI(),
                CorrelationIdHolder.get()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.of(
                "Unauthorized",
                HttpStatus.UNAUTHORIZED.value(),
                "Kimlik doğrulama gerekli",
                request.getRequestURI(),
                CorrelationIdHolder.get()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        ErrorResponse body = ErrorResponse.of(
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Beklenmeyen bir hata oluştu",
                request.getRequestURI(),
                CorrelationIdHolder.get()
        );
        return ResponseEntity.internalServerError().body(body);
    }

    private String describe(FieldError fe) {
        return fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "geçersiz değer";
    }
}
