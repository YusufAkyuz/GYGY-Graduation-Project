package com.telcox.ticketservice.web;

import com.telcox.ticketservice.dto.CreateTicketRequest;
import com.telcox.ticketservice.dto.TicketResponse;
import com.telcox.ticketservice.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse open(@Valid @RequestBody CreateTicketRequest request) {
        return TicketResponse.from(ticketService.open(request));
    }

    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable UUID id) {
        return TicketResponse.from(ticketService.getById(id));
    }

    @PostMapping("/{id}/resolve")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public TicketResponse resolve(@PathVariable UUID id) {
        return TicketResponse.from(ticketService.resolve(id));
    }
}
