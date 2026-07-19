package com.telcox.billingservice.repository;

import com.telcox.billingservice.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    boolean existsBySubscriptionIdAndBillingPeriod(UUID subscriptionId, String billingPeriod);
}
