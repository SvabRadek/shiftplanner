package com.cocroachden.planner.tickets.command.issueticket;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.tickets.Ticket;

public record TicketHasBeenIssued(
        Ticket ticket
) implements Event {
}
