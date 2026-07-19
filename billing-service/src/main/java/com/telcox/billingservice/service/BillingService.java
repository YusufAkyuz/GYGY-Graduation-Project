package com.telcox.billingservice.service;

import com.telcox.billingservice.client.ProductClient;
import com.telcox.billingservice.client.SubscriptionClient;
import com.telcox.billingservice.client.SubscriptionClientResponse;
import com.telcox.billingservice.client.UsageClient;
import com.telcox.billingservice.domain.Invoice;
import com.telcox.billingservice.event.InvoiceGeneratedEvent;
import com.telcox.billingservice.repository.InvoiceRepository;
import com.telcox.common.error.ApiException;
import com.telcox.common.outbox.OutboxEventPublisherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

/**
 * FR-21: aylık bill-run — tüm aktif abonelikler için fatura kesimi. MVP'de manuel tetiklenir
 * (scheduler yerine POST /api/v1/billing/run); NFR-02 (100K abone < 30dk) için üretimde
 * batch/chunk işlemeye evrilecek şekilde tasarlanmıştır (her abonelik bağımsız transaction).
 */
@Service
public class BillingService {

    private static final String AGGREGATE_TYPE = "Invoice";
    private static final BigDecimal OVERAGE_RATE_PER_MB = new BigDecimal("0.05");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.20");

    private final InvoiceRepository invoiceRepository;
    private final SubscriptionClient subscriptionClient;
    private final ProductClient productClient;
    private final UsageClient usageClient;
    private final OutboxEventPublisherService outboxEventPublisherService;

    public BillingService(InvoiceRepository invoiceRepository, SubscriptionClient subscriptionClient,
                           ProductClient productClient, UsageClient usageClient,
                           OutboxEventPublisherService outboxEventPublisherService) {
        this.invoiceRepository = invoiceRepository;
        this.subscriptionClient = subscriptionClient;
        this.productClient = productClient;
        this.usageClient = usageClient;
        this.outboxEventPublisherService = outboxEventPublisherService;
    }

    public int runBillCycle(YearMonth period) {
        List<SubscriptionClientResponse> subscriptions = subscriptionClient.listAllActive();
        int generated = 0;
        for (SubscriptionClientResponse sub : subscriptions) {
            if (generateInvoiceForSubscription(sub, period)) {
                generated++;
            }
        }
        return generated;
    }

    @Transactional
    public boolean generateInvoiceForSubscription(SubscriptionClientResponse sub, YearMonth period) {
        if (invoiceRepository.existsBySubscriptionIdAndBillingPeriod(sub.id(), period.toString())) {
            return false; // idempotent: bu dönem için zaten fatura kesilmiş
        }

        BigDecimal monthlyFee = productClient.getMonthlyPrice(sub.productCode());
        BigDecimal overageMb = usageClient.getOverageMb(sub.id());
        BigDecimal overageFee = overageMb.multiply(OVERAGE_RATE_PER_MB).setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxAmount = monthlyFee.add(overageFee).multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = monthlyFee.add(overageFee).add(taxAmount);

        Invoice invoice = new Invoice(sub.id(), sub.customerId(), period, monthlyFee, overageFee, taxAmount, total);
        invoice = invoiceRepository.save(invoice);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, invoice.getId().toString(), "InvoiceGenerated",
                new InvoiceGeneratedEvent(invoice.getId(), invoice.getCustomerId(), invoice.getSubscriptionId(),
                        invoice.getTotalAmount(), invoice.getBillingPeriod()));

        return true;
    }

    @Transactional(readOnly = true)
    public Invoice getById(UUID id) {
        return invoiceRepository.findById(id).orElseThrow(() -> ApiException.notFound("Fatura bulunamadı: " + id));
    }

    @Transactional
    public Invoice markPaid(UUID id) {
        Invoice invoice = getById(id);
        invoice.markPaid();
        return invoiceRepository.save(invoice);
    }
}
