package com.cocroachden.planner.tickets.command;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.tickets.Ticket;

public record TicketHasExpired(Ticket ticket) implements Event {
}
