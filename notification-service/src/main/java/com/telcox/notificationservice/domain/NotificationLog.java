package com.telcox.notificationservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/** FR-28: mock kanal üzerinden gönderim kaydı (gerçek SMS/e-posta sağlayıcısına bağlanmaz). */
@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "template_code", nullable = false)
    private String templateCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "sent_at", nullable = false, updatable = false)
    private Instant sentAt = Instant.now();

    protected NotificationLog() {
    }

    public NotificationLog(String templateCode, NotificationChannel channel, String recipient, String content) {
        this.templateCode = templateCode;
        this.channel = channel;
        this.recipient = recipient;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }

    public Instant getSentAt() {
        return sentAt;
    }
}
