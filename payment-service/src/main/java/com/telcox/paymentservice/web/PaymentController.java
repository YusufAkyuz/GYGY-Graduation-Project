package com.telcox.paymentservice.web;

import com.telcox.common.error.ApiException;
import com.telcox.paymentservice.dto.PaymentResponse;
import com.telcox.paymentservice.repository.PaymentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/by-order/{orderId}")
    public PaymentResponse getByOrder(@PathVariable UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(PaymentResponse::from)
                .orElseThrow(() -> ApiException.notFound("Bu sipariş için ödeme bulunamadı: " + orderId));
    }
}
