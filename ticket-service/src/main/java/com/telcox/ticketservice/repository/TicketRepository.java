package com.telcox.ticketservice.repository;

import com.telcox.ticketservice.domain.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Page<Ticket> findAllByCustomerId(UUID customerId, Pageable pageable);
}
