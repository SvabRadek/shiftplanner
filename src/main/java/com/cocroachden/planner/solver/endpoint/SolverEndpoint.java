package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.security.Role;
import com.cocroachden.planner.solver.SolutionStatus;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.command.solveconfiguration.SolutionHasBeenFound;
import com.cocroachden.planner.solver.command.solveconfiguration.StartSolverCommand;
import com.cocroachden.planner.solver.command.stopsolver.StopSolverCommand;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.query.SolverConfigurationQuery;
import com.cocroachden.planner.tickets.Ticket;
import com.cocroachden.planner.tickets.query.TicketQuery;
import com.cocroachden.planner.user.query.RegisteredUserQuery;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@BrowserCallable
@Slf4j
@RolesAllowed(Role.ROLE_USER)
public class SolverEndpoint {

    private final Integer solverDefaultTimeLimitInSec;
    private final ApplicationEventPublisher publisher;
    private final Flux<SolverSolutionDTO> solutionFlux;
    private final Sinks.Many<SolverSolutionDTO> sink;
    private final TicketQuery ticketQuery;
    private final RegisteredUserQuery registeredUserQuery;
    private final SolverConfigurationQuery configurationQuery;

    public SolverEndpoint(
            ApplicationEventPublisher publisher,
            TicketQuery ticketQuery,
            RegisteredUserQuery registeredUserQuery,
            @Value("${application.solver.default-solver-time-limit-in-sec}") Integer solverDefaultTimeLimitInSec,
            SolverConfigurationQuery configurationQuery
    ) {
        this.publisher = publisher;
        this.ticketQuery = ticketQuery;
        this.registeredUserQuery = registeredUserQuery;
        this.configurationQuery = configurationQuery;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.solutionFlux = sink.asFlux().takeWhile(solution -> solution.getSolutionStatus().equals(SolutionStatus.OK));
        this.solverDefaultTimeLimitInSec = solverDefaultTimeLimitInSec;
    }

    public EndpointSubscription<@Nonnull SolverSolutionDTO> solveProblem(String configurationId, String ticket) {
        var username = ticketQuery.getUsernameForTicket(Ticket.from(ticket))
                .orElseThrow(() -> new IllegalArgumentException("Ticket " + ticket + " is invalid!"));
        if (!registeredUserQuery.hasRole(username, Authorities.USER)) {
            throw new IllegalArgumentException("User " + username + " is not authorized to start solver!");
        }
        var configuration = configurationQuery.getSolverConfigurationById(username, SolverConfigurationId.from(configurationId));
        SolverSubscriptionId subscriptionId = SolverSubscriptionId.random();
        var command = new StartSolverCommand(
                configuration,
                subscriptionId,
                solverDefaultTimeLimitInSec,
                username
        );
        publisher.publishEvent(command);
        return EndpointSubscription.of(
                solutionFlux,
                () -> publisher.publishEvent(new StopSolverCommand(subscriptionId, username))
        );
    }

    @EventListener
    protected void on(SolutionHasBeenFound event) {
        sink.tryEmitNext(event.solution());
    }
}
