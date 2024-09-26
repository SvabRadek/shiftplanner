package com.cocroachden.planner.tickets.service;

import com.cocroachden.planner.AbstractMessagingTest;
import com.cocroachden.planner.tickets.Ticket;
import com.cocroachden.planner.tickets.command.TicketHasExpired;
import com.cocroachden.planner.tickets.command.issueticket.IssueTicketCommand;
import com.cocroachden.planner.tickets.command.issueticket.TicketHasBeenIssued;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        "application.tickets.expiration-time-in-sec=0"
})
class TicketServiceTest extends AbstractMessagingTest {

    @Test
    public void itCanIssueExpiringTicket() {
        Ticket ticket = Ticket.random();
        var command = new IssueTicketCommand(ticket, "irrelevant");
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(TicketHasBeenIssued.class);
        this.thenSomeTimeHasPassed(10);
        this.thenExactlyOneEventHasBeenDispatched(TicketHasExpired.class);
    }

}