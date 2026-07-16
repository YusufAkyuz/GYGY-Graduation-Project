package com.telcox.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.telcox.common.jpa.CommonJpaAutoConfiguration;

@AutoConfiguration
@AutoConfigureAfter(CommonJpaAutoConfiguration.class)
@ConditionalOnClass({KafkaTemplate.class, jakarta.persistence.Entity.class})
@EnableConfigurationProperties(OutboxPublisherProperties.class)
@EnableScheduling
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
