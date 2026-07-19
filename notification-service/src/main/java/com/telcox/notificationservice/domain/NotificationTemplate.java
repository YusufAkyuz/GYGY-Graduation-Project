package com.telcox.notificationservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** FR-29: şablon tabanlı bildirim yönetimi. "{{key}}" placeholder'ları çalışma zamanında değiştirilir. */
@Entity
@Table(name = "notification_templates")
public class NotificationTemplate {

    @Id
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Column(nullable = false, length = 1000)
    private String content;

    protected NotificationTemplate() {
    }

    public NotificationTemplate(String code, NotificationChannel channel, String content) {
        this.code = code;
        this.channel = channel;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public String getContent() {
        return content;
    }
}
