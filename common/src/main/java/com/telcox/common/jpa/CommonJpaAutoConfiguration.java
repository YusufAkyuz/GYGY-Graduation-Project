package com.telcox.common.jpa;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * common modülündeki JPA entity/repository'lerinin (Outbox, Audit) her serviste ayrı
 * @EntityScan/@EnableJpaRepositories bildirmeye gerek kalmadan otomatik taranmasını sağlar.
 * Servisin kendi @EnableJpaRepositories'i (Boot'un otomatik olanı) ile bizim ayrı bir tane
 * daha eklememiz aynı context'te çakışıp servisin kendi repository'lerinin kaybolmasına yol
 * açtığından (bkz. customer-service'te CustomerRepository bulunamama hatası), bunun yerine
 * "com.telcox.common" paketini Boot'un auto-configuration base package listesine ekleyip
 * TEK bir tarama mekanizmasının hem servisin hem common'ın paketlerini kapsamasını sağlıyoruz.
 */
@AutoConfiguration
@ConditionalOnClass(jakarta.persistence.Entity.class)
@AutoConfigureBefore(name = {
        "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration",
        "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
@Import(CommonJpaAutoConfiguration.PackageRegistrar.class)
public class CommonJpaAutoConfiguration {

    static class PackageRegistrar implements ImportBeanDefinitionRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            AutoConfigurationPackages.register(registry, "com.telcox.common.outbox", "com.telcox.common.audit");
        }
    }
}
