package com.telcox.usageservice.service;

import com.telcox.common.error.ApiException;
import com.telcox.common.outbox.OutboxEventPublisherService;
import com.telcox.usageservice.domain.UsageBalance;
import com.telcox.usageservice.dto.CdrRequest;
import com.telcox.usageservice.event.QuotaExceededEvent;
import com.telcox.usageservice.event.QuotaThresholdReachedEvent;
import com.telcox.usageservice.repository.UsageBalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UsageService {

    private static final String AGGREGATE_TYPE = "UsageBalance";
    private static final BigDecimal DEFAULT_QUOTA_MB = BigDecimal.valueOf(5000);

    private final UsageBalanceRepository usageBalanceRepository;
    private final OutboxEventPublisherService outboxEventPublisherService;

    public UsageService(UsageBalanceRepository usageBalanceRepository, OutboxEventPublisherService outboxEventPublisherService) {
        this.usageBalanceRepository = usageBalanceRepository;
        this.outboxEventPublisherService = outboxEventPublisherService;
    }

    @Transactional
    public UsageBalance recordCdr(CdrRequest request) {
        UsageBalance balance = usageBalanceRepository.findById(request.subscriptionId())
                .orElseGet(() -> new UsageBalance(request.subscriptionId(), request.msisdn(), DEFAULT_QUOTA_MB));

        boolean wasAbove80 = balance.crossed80();
        boolean wasAbove100 = balance.crossed100();

        balance.recordUsage(request.dataMb());

        if (!wasAbove80 && balance.crossed80() && !balance.isThreshold80Notified()) {
            balance.markThreshold80Notified();
            outboxEventPublisherService.publish(AGGREGATE_TYPE, balance.getSubscriptionId().toString(),
                    "QuotaThresholdReached",
                    new QuotaThresholdReachedEvent(balance.getSubscriptionId(), balance.getMsisdn(), 80, balance.getUsedMb()));
        }

        if (!wasAbove100 && balance.crossed100() && !balance.isThreshold100Notified()) {
            balance.markThreshold100Notified();
            outboxEventPublisherService.publish(AGGREGATE_TYPE, balance.getSubscriptionId().toString(),
                    "QuotaExceeded", new QuotaExceededEvent(balance.getSubscriptionId(), balance.getMsisdn(), balance.getOverageMb()));
        }

        return usageBalanceRepository.save(balance);
    }

    @Transactional(readOnly = true)
    public UsageBalance getBalance(UUID subscriptionId) {
        return usageBalanceRepository.findById(subscriptionId)
                .orElseThrow(() -> ApiException.notFound("Kullanım bilgisi bulunamadı: " + subscriptionId));
    }
}
