package com.telcox.notificationservice.dto;

import com.telcox.notificationservice.domain.NotificationChannel;
import com.telcox.notificationservice.domain.NotificationLog;

import java.time.Instant;
import java.util.UUID;

public record NotificationLogResponse(UUID id, String templateCode, NotificationChannel channel, String recipient,
                                       String content, Instant sentAt) {
    public static NotificationLogResponse from(NotificationLog n) {
        return new NotificationLogResponse(n.getId(), n.getTemplateCode(), n.getChannel(), n.getRecipient(),
                n.getContent(), n.getSentAt());
    }
}
