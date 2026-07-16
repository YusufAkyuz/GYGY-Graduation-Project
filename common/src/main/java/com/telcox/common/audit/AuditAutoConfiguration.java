package com.telcox.common.audit;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import com.telcox.common.jpa.CommonJpaAutoConfiguration;

@AutoConfiguration
@AutoConfigureAfter(CommonJpaAutoConfiguration.class)
@ConditionalOnClass(jakarta.persistence.Entity.class)
public class AuditAutoConfiguration {

    @Bean
    public AuditService auditService(AuditLogEntryRepository repository) {
        return new AuditService(repository);
    }
}
