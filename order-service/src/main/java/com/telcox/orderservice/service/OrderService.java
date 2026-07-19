package com.telcox.orderservice.service;

import com.telcox.common.error.ApiException;
import com.telcox.common.outbox.OutboxEventPublisherService;
import com.telcox.orderservice.client.CustomerClient;
import com.telcox.orderservice.client.CustomerClientResponse;
import com.telcox.orderservice.client.ProductClient;
import com.telcox.orderservice.client.ProductClientResponse;
import com.telcox.orderservice.domain.Order;
import com.telcox.orderservice.dto.CreateOrderRequest;
import com.telcox.orderservice.event.OrderCreatedEvent;
import com.telcox.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Saga'nın başlatıcısı (TR-04): sipariş oluşturulmadan önce müşteri (REST) ve katalog (REST)
 * senkron doğrulanır; ardından OrderCreated event'i outbox ile Kafka'ya yayınlanır ve
 * payment-service ile subscription-service bu event'i asenkron tüketir.
 */
@Service
public class OrderService {

    private static final String AGGREGATE_TYPE = "Order";

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OutboxEventPublisherService outboxEventPublisherService;

    public OrderService(OrderRepository orderRepository, CustomerClient customerClient,
                         ProductClient productClient, OutboxEventPublisherService outboxEventPublisherService) {
        this.orderRepository = orderRepository;
        this.customerClient = customerClient;
        this.productClient = productClient;
        this.outboxEventPublisherService = outboxEventPublisherService;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        CustomerClientResponse customer = customerClient.getActiveCustomer(request.customerId());
        if (!customer.isActive()) {
            throw ApiException.conflict("Müşteri ACTIVE durumda değil, sipariş oluşturulamaz");
        }

        ProductClientResponse product = productClient.getCurrentProduct(request.productCode());
        if (!product.isCurrent()) {
            throw ApiException.conflict("Ürün artık satışta değil");
        }

        Order order = new Order(request.customerId(), product.code(), product.id(),
                product.monthlyPrice(), product.currency());
        order.markPendingPayment();
        order = orderRepository.save(order);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, order.getId().toString(), "OrderCreated",
                new OrderCreatedEvent(order.getId(), order.getCustomerId(), order.getProductCode(),
                        order.getProductVersionId(), order.getAmount(), order.getCurrency()));

        return order;
    }

    @Transactional(readOnly = true)
    public Order getById(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> ApiException.notFound("Sipariş bulunamadı: " + id));
    }

    @Transactional
    public void markPaid(UUID orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            if (order.getStatus() == com.telcox.orderservice.domain.OrderStatus.PENDING_PAYMENT) {
                order.markPaid();
                orderRepository.save(order);
            }
        });
    }

    @Transactional
    public void markFulfilled(UUID orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            if (order.getStatus() == com.telcox.orderservice.domain.OrderStatus.PAID) {
                order.markFulfilled();
                orderRepository.save(order);
            }
        });
    }

    @Transactional
    public void cancel(UUID orderId, String reason) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.cancel(reason);
            orderRepository.save(order);
        });
    }
}
