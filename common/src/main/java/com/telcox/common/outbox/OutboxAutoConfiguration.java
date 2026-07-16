package com.telcox.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfiguration
@ConditionalOnClass({KafkaTemplate.class, jakarta.persistence.Entity.class})
@EnableConfigurationProperties(OutboxPublisherProperties.class)
@EnableScheduling
@EntityScan(basePackageClasses = OutboxEvent.class)
@EnableJpaRepositories(basePackageClasses = OutboxEventRepository.class)
public class OutboxAutoConfiguration {

    @Bean
    public OutboxEventPublisherService outboxEventPublisherService(OutboxEventRepository repository, ObjectMapper objectMapper) {
        return new OutboxEventPublisherService(repository, objectMapper);
    }

    @Bean
    public OutboxPollingPublisher outboxPollingPublisher(OutboxEventRepository repository,
                                                           KafkaTemplate<String, String> kafkaTemplate,
                                                           OutboxPublisherProperties properties) {
        return new OutboxPollingPublisher(repository, kafkaTemplate, properties);
    }
}
