package com.telcox.orderservice.domain;

/** FR-11: Sipariş durumları. */
public enum OrderStatus {
    DRAFT,
    PENDING_PAYMENT,
    PAID,
    FULFILLED,
    CANCELLED
}
