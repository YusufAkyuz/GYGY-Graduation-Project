package com.telcox.usageservice.event;

import java.math.BigDecimal;
import java.util.UUID;

public record QuotaExceededEvent(UUID subscriptionId, String msisdn, BigDecimal overageMb) {
}
