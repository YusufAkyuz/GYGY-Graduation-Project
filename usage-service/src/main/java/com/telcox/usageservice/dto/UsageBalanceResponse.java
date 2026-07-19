package com.telcox.usageservice.dto;

import com.telcox.usageservice.domain.UsageBalance;

import java.math.BigDecimal;
import java.util.UUID;

public record UsageBalanceResponse(UUID subscriptionId, String msisdn, BigDecimal quotaMb, BigDecimal usedMb,
                                    BigDecimal overageMb) {
    public static UsageBalanceResponse from(UsageBalance b) {
        return new UsageBalanceResponse(b.getSubscriptionId(), b.getMsisdn(), b.getQuotaMb(), b.getUsedMb(), b.getOverageMb());
    }
}
