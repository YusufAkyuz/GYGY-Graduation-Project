package com.telcox.ticketservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/** FR-31..FR-33: talep kaydı, SLA bazlı otomatik atama. */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    @Column(name = "assigned_team")
    private String assignedTeam;

    @Column(name = "sla_due_at", nullable = false)
    private Instant slaDueAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant resolvedAt;

    protected Ticket() {
    }

    public Ticket(UUID customerId, String subject, String description) {
        this.customerId = customerId;
        this.subject = subject;
        this.description = description;
        this.slaDueAt = Instant.now().plus(24, ChronoUnit.HOURS);
    }

    /** FR-32: basit kural tabanlı otomatik atama (MVP'de tek takım havuzu). */
    public void autoAssign() {
        this.assignedTeam = "SUPPORT_L1";
        this.status = TicketStatus.ASSIGNED;
    }

    public void resolve() {
        this.status = TicketStatus.RESOLVED;
        this.resolvedAt = Instant.now();
    }

    public boolean isSlaBreached() {
        return status != TicketStatus.RESOLVED && Instant.now().isAfter(slaDueAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public Instant getSlaDueAt() {
        return slaDueAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }
}
