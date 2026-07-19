package com.telcox.subscriptionservice.web;

import com.telcox.common.web.PageResponse;
import com.telcox.subscriptionservice.dto.SubscriptionResponse;
import com.telcox.subscriptionservice.service.SubscriptionService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/by-order/{orderId}")
    public SubscriptionResponse getByOrder(@PathVariable UUID orderId) {
        return SubscriptionResponse.from(subscriptionService.getByOrderId(orderId));
    }

    /** billing-service bill-run için kullanır; ayrıca admin/agent aracılığıyla da erişilebilir. */
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public PageResponse<SubscriptionResponse> listActive(Pageable pageable) {
        return PageResponse.from(subscriptionService.listActive(pageable).map(SubscriptionResponse::from));
    }
}
