package com.telcox.notificationservice.web;

import com.telcox.common.web.PageResponse;
import com.telcox.notificationservice.dto.NotificationLogResponse;
import com.telcox.notificationservice.repository.NotificationLogRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationLogController {

    private final NotificationLogRepository notificationLogRepository;

    public NotificationLogController(NotificationLogRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<NotificationLogResponse> list(@RequestParam String recipient, Pageable pageable) {
        return PageResponse.from(notificationLogRepository.findAllByRecipient(recipient, pageable).map(NotificationLogResponse::from));
    }
}
