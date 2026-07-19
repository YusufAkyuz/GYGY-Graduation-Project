package com.telcox.orderservice.client;

import java.util.UUID;

public record CustomerClientResponse(UUID id, String status) {

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
}
