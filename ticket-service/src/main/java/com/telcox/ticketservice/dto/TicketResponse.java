package com.telcox.ticketservice.dto;

import com.telcox.ticketservice.domain.Ticket;
import com.telcox.ticketservice.domain.TicketStatus;

import java.time.Instant;
import java.util.UUID;

public record TicketResponse(UUID id, UUID customerId, String subject, String description, TicketStatus status,
                              String assignedTeam, Instant slaDueAt, Instant createdAt, Instant resolvedAt) {
    public static TicketResponse from(Ticket t) {
        return new TicketResponse(t.getId(), t.getCustomerId(), t.getSubject(), t.getDescription(), t.getStatus(),
                t.getAssignedTeam(), t.getSlaDueAt(), t.getCreatedAt(), t.getResolvedAt());
    }
}
