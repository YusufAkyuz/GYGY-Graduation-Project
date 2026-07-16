package com.telcox.common.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * SR-06: identity, customer, payment, subscription servislerinde her değişikliğin
 * audit_log tablosuna yazılması. Her servis kendi Flyway migration'ında bu şekilde
 * bir "audit_log" tablosu oluşturmalıdır (bkz. identity-service V1 migration).
 */
@Entity
@Table(name = "audit_log")
public class AuditLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "actor_user_id")
    private String actorUserId;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected AuditLogEntry() {
    }

    public AuditLogEntry(String entityName, String entityId, String action, String actorUserId, String details) {
        this.entityName = entityName;
        this.entityId = entityId;
        this.action = action;
        this.actorUserId = actorUserId;
        this.details = details;
    }
}
