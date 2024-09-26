package com.cocroachden.planner.tickets.service;

import com.cocroachden.planner.tickets.Ticket;
import com.cocroachden.planner.tickets.command.TicketHasExpired;
import com.cocroachden.planner.tickets.command.issueticket.IssueTicketCommand;
import com.cocroachden.planner.tickets.command.issueticket.TicketHasBeenIssued;
import com.cocroachden.planner.tickets.configuration.TicketModuleConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class TicketService {

    @Getter
    private final Map<Ticket, String> tickets = new ConcurrentHashMap<>();
    private final TicketModuleConfiguration ticketModuleConfiguration;
    private final ApplicationEventPublisher publisher;

    @EventListener
    public TicketHasBeenIssued handle(IssueTicketCommand command) {
        log.debug("Handling IssueTicketCommand...");
        tickets.put(command.ticket(), command.username());
        var delayedExecutor = CompletableFuture.delayedExecutor(ticketModuleConfiguration.getExpirationTimeInSec(), TimeUnit.SECONDS);
        CompletableFuture.runAsync(() -> {
                    tickets.remove(command.ticket());
                    log.debug("Ticket [{}] expired.", command.ticket().value());
                    publisher.publishEvent(new TicketHasExpired(command.ticket()));
                }, delayedExecutor
        );
        return new TicketHasBeenIssued(command.ticket());
    }

}
