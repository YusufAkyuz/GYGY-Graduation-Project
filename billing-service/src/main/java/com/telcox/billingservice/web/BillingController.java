package com.telcox.billingservice.web;

import com.telcox.billingservice.dto.BillRunResponse;
import com.telcox.billingservice.dto.InvoiceResponse;
import com.telcox.billingservice.service.BillingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    /** FR-21: manuel bill-run tetikleme (üretimde aylık scheduler ile de tetiklenebilir). */
    @PostMapping("/run")
    @PreAuthorize("hasRole('ADMIN')")
    public BillRunResponse runBillCycle() {
        YearMonth period = YearMonth.now();
        int generated = billingService.runBillCycle(period);
        return new BillRunResponse(period.toString(), generated);
    }

    @GetMapping("/invoices/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','BILLING_OPERATOR')")
    public InvoiceResponse getInvoice(@PathVariable UUID id) {
        return InvoiceResponse.from(billingService.getById(id));
    }

    @PostMapping("/invoices/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN','BILLING_OPERATOR')")
    public InvoiceResponse markPaid(@PathVariable UUID id) {
        return InvoiceResponse.from(billingService.markPaid(id));
    }
}
