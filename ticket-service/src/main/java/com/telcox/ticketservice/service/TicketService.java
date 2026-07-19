package com.telcox.ticketservice.service;

import com.telcox.common.error.ApiException;
import com.telcox.common.outbox.OutboxEventPublisherService;
import com.telcox.ticketservice.domain.Ticket;
import com.telcox.ticketservice.dto.CreateTicketRequest;
import com.telcox.ticketservice.event.TicketOpenedEvent;
import com.telcox.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TicketService {

    private static final String AGGREGATE_TYPE = "Ticket";

    private final TicketRepository ticketRepository;
    private final OutboxEventPublisherService outboxEventPublisherService;

    public TicketService(TicketRepository ticketRepository, OutboxEventPublisherService outboxEventPublisherService) {
        this.ticketRepository = ticketRepository;
        this.outboxEventPublisherService = outboxEventPublisherService;
    }

    @Transactional
    public Ticket open(CreateTicketRequest request) {
        Ticket ticket = new Ticket(request.customerId(), request.subject(), request.description());
        ticket.autoAssign(); // FR-32: SLA bazlı otomatik ekip ataması
        ticket = ticketRepository.save(ticket);

        outboxEventPublisherService.publish(AGGREGATE_TYPE, ticket.getId().toString(), "TicketOpened",
                new TicketOpenedEvent(ticket.getId(), ticket.getCustomerId(), ticket.getSubject()));

        return ticket;
    }

    @Transactional
    public Ticket resolve(UUID id) {
        Ticket ticket = getById(id);
        ticket.resolve();
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public Ticket getById(UUID id) {
        return ticketRepository.findById(id).orElseThrow(() -> ApiException.notFound("Talep bulunamadı: " + id));
    }
}
