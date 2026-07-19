package com.telcox.subscriptionservice.web;

import com.telcox.subscriptionservice.dto.SubscriptionResponse;
import com.telcox.subscriptionservice.service.SubscriptionService;
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
}
