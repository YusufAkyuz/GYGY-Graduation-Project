package com.telcox.common.outbox;

import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Outbox tablosunu periyodik olarak tarayıp yayınlanmamış event'leri Kafka'ya publish eder,
 * başarılı gönderimden sonra processedAt'i işaretler (at-least-once teslimat).
 * Idempotent consumer varsayımıyla downstream'de tekrar işlemenin önüne geçilir (NFR-11).
 * Bean tanımı OutboxAutoConfiguration üzerinden yapılır.
 */
public class OutboxPollingPublisher {

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxPublisherProperties properties;

    public OutboxPollingPublisher(OutboxEventRepository repository, KafkaTemplate<String, String> kafkaTemplate,
                                   OutboxPublisherProperties properties) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    @Scheduled(fixedDelayString = "${telcox.outbox.poll-interval-ms:2000}")
    @Transactional
    public void publishPendingEvents() {
        if (!properties.isEnabled()) {
            return;
        }
        List<OutboxEvent> batch = repository.findUnprocessedBatch(PageRequest.of(0, properties.getBatchSize()));
        for (OutboxEvent event : batch) {
            String topic = properties.getTopicPrefix() + event.getEventType();
            kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload());
            event.markProcessed();
        }
        if (!batch.isEmpty()) {
            repository.saveAll(batch);
        }
    }
}
