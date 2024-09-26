package com.cocroachden.planner.tickets.endpoint;

import com.cocroachden.planner.security.Role;
import com.cocroachden.planner.tickets.Ticket;
import com.cocroachden.planner.tickets.command.issueticket.IssueTicketCommand;
import com.vaadin.flow.server.VaadinRequest;
import dev.hilla.Endpoint;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@Endpoint
@AllArgsConstructor
public class TicketEndpoint {
    private final ApplicationEventPublisher publisher;

    @RolesAllowed({ Role.ROLE_USER })
    public String issueTicket() {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        var ticket = Ticket.random();
        publisher.publishEvent(new IssueTicketCommand(ticket, username));
        return ticket.value();
    }

}
