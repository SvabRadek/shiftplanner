package com.cocroachden.planner.tickets.query;

import com.cocroachden.planner.tickets.Ticket;
import com.cocroachden.planner.tickets.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class TicketQuery {

    private final TicketService ticketService;

    public Boolean isTicketValid(Ticket ticket) {
        return ticketService.getTickets().containsKey(ticket);
    }

    public Optional<String> getUsernameForTicket(Ticket ticket) {
        return Optional.ofNullable(ticketService.getTickets().getOrDefault(ticket, null));
    }

}
