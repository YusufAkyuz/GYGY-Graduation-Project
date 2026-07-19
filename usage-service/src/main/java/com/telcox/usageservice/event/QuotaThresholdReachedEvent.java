package com.telcox.usageservice.event;

import java.math.BigDecimal;
import java.util.UUID;

public record QuotaThresholdReachedEvent(UUID subscriptionId, String msisdn, int thresholdPercent, BigDecimal usedMb) {
}
