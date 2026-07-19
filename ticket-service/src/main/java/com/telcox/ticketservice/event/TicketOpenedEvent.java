package com.telcox.ticketservice.event;

import java.util.UUID;

public record TicketOpenedEvent(UUID ticketId, UUID customerId, String subject) {
}
