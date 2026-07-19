package com.telcox.notificationservice.repository;

import com.telcox.notificationservice.domain.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, java.util.UUID> {

    Page<NotificationLog> findAllByRecipient(String recipient, Pageable pageable);
}
