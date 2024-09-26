package com.cocroachden.planner.tickets.command.issueticket;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.tickets.Ticket;

public record IssueTicketCommand(
        Ticket ticket,
        String username
) implements Command {
}
