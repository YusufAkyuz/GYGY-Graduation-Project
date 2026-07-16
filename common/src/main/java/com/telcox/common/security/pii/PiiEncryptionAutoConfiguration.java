package com.telcox.common.security.pii;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;

@AutoConfiguration
@ConditionalOnClass(jakarta.persistence.AttributeConverter.class)
@EnableConfigurationProperties(PiiEncryptionProperties.class)
public class PiiEncryptionAutoConfiguration {

    private final PiiEncryptionProperties properties;

    public PiiEncryptionAutoConfiguration(PiiEncryptionProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void initKeyHolder() {
        if (StringUtils.hasText(properties.getPiiEncryptionKey())) {
            PiiKeyHolder.init(properties.getPiiEncryptionKey());
        }
    }
}
