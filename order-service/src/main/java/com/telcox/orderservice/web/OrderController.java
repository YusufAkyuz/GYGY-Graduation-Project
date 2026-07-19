package com.telcox.orderservice.web;

import com.telcox.orderservice.dto.CreateOrderRequest;
import com.telcox.orderservice.dto.OrderResponse;
import com.telcox.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return OrderResponse.from(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable UUID id) {
        return OrderResponse.from(orderService.getById(id));
    }
}
