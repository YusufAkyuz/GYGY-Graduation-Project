package com.telcox.notificationservice.repository;

import com.telcox.notificationservice.domain.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, String> {
}
