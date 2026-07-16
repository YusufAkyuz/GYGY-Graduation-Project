package com.telcox.common.audit;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnClass(jakarta.persistence.Entity.class)
@EntityScan(basePackageClasses = AuditLogEntry.class)
@EnableJpaRepositories(basePackageClasses = AuditLogEntryRepository.class)
public class AuditAutoConfiguration {

    @Bean
    public AuditService auditService(AuditLogEntryRepository repository) {
        return new AuditService(repository);
    }
}
