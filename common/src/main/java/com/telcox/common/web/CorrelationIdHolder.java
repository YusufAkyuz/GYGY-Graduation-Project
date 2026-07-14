package com.telcox.common.web;

/**
 * Correlation-Id'yi istek thread'i boyunca taşıyan basit ThreadLocal tutucu.
 * TR-20: tüm loglara ve hata yanıtlarına aynı correlation id yazılır.
 */
public final class CorrelationIdHolder {

    public static final String HEADER_NAME = "X-Correlation-Id";
    public static final String MDC_KEY = "correlationId";

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    private CorrelationIdHolder() {
    }

    public static void set(String correlationId) {
        HOLDER.set(correlationId);
    }

    public static String get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
