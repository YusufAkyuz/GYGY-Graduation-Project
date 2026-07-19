package com.telcox.common.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.boot.jackson2.autoconfigure.Jackson2ObjectMapperBuilderCustomizer;

/**
 * Kafka event consumer'ları, üreten servisin event'e sonradan eklediği yeni alanlardan
 * etkilenmemesi için sadece ihtiyaç duyduğu alt kümeyi bağlayabilsin diye (forward-compatible
 * tüketim), varsayılan FAIL_ON_UNKNOWN_PROPERTIES kapatılır.
 */
@AutoConfiguration
@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
public class CommonJacksonAutoConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer forwardCompatibleJacksonCustomizer() {
        return builder -> builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
