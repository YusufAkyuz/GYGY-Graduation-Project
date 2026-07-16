package com.telcox.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Domain event'lerini aynı transaction içinde outbox tablosuna yazar. Çağıran taraf
 * (ör. CustomerService) entity kaydını ve bu çağrıyı aynı @Transactional metotta yapmalıdır.
 * Bean tanımı OutboxAutoConfiguration üzerinden yapılır.
 */
public class OutboxEventPublisherService {

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxEventPublisherService(OutboxEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void publish(String aggregateType, String aggregateId, String eventType, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            repository.save(new OutboxEvent(aggregateType, aggregateId, eventType, json));
        } catch (Exception e) {
            throw new IllegalStateException("Outbox event serileştirilemedi: " + eventType, e);
        }
    }
}
