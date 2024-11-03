package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.security.Role;
import com.cocroachden.planner.solver.SolutionNotificationDTO;
import com.cocroachden.planner.solver.SolutionStatus;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.command.solveconfiguration.SolutionHasBeenFound;
import com.cocroachden.planner.solver.command.solveconfiguration.StartSolverCommand;
import com.cocroachden.planner.solver.command.stopsolver.SolverHasBeenStopped;
import com.cocroachden.planner.solver.command.stopsolver.StopSolverCommand;
import com.cocroachden.planner.solver.query.SolverServiceQuery;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.query.SolverConfigurationQuery;
import com.cocroachden.planner.tickets.Ticket;
import com.cocroachden.planner.tickets.query.TicketQuery;
import com.cocroachden.planner.user.query.RegisteredUserQuery;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@BrowserCallable
@Slf4j
@Service
public class SolverEndpoint {

    private final Integer solverDefaultTimeLimitInSec;
    private final ApplicationEventPublisher publisher;
    private Sinks.Many<SolutionNotificationDTO> sink;
    private final TicketQuery ticketQuery;
    private final RegisteredUserQuery registeredUserQuery;
    private final SolverConfigurationQuery configurationQuery;
    private final SolverServiceQuery solverServiceQuery;

    public SolverEndpoint(
            ApplicationEventPublisher publisher,
            TicketQuery ticketQuery,
            RegisteredUserQuery registeredUserQuery,
            @Value("${application.solver.default-solver-time-limit-in-sec}") Integer solverDefaultTimeLimitInSec,
            SolverConfigurationQuery configurationQuery,
            SolverServiceQuery solverServiceQuery
    ) {
        this.publisher = publisher;
        this.ticketQuery = ticketQuery;
        this.solverServiceQuery = solverServiceQuery;
        this.registeredUserQuery = registeredUserQuery;
        this.configurationQuery = configurationQuery;
        this.solverDefaultTimeLimitInSec = solverDefaultTimeLimitInSec;
    }

    @AnonymousAllowed
    public @Nonnull EndpointSubscription<@Nonnull SolutionNotificationDTO> solveSavedProblem(@Nonnull String configurationId, @Nonnull String ticket) {
        var username = ticketQuery.getUsernameForTicket(Ticket.from(ticket))
                                  .orElseThrow(() -> new IllegalArgumentException("Ticket " + ticket + " is invalid!"));
        if (!registeredUserQuery.hasRole(username, Authorities.USER)) {
            throw new IllegalArgumentException("User " + username + " is not authorized to start solver!");
        }
        var configuration = configurationQuery.getSolverConfigurationById(username, SolverConfigurationId.from(configurationId));
        return this.solveProblem(configuration, ticket);
    }

    @AnonymousAllowed
    public @Nonnull EndpointSubscription<@Nonnull SolutionNotificationDTO> solveProblem(@Nonnull SolverConfigurationDTO configuration, @Nonnull String ticket) {
        log.debug("Handling solveProblem endpoint...");
        var username = ticketQuery.getUsernameForTicket(Ticket.from(ticket))
                                  .orElseThrow(() -> new IllegalArgumentException("Ticket " + ticket + " is invalid!"));
        if (!registeredUserQuery.hasRole(username, Authorities.USER)) {
            throw new IllegalArgumentException("User " + username + " is not authorized to start solver!");
        }
        SolverSubscriptionId subscriptionId = SolverSubscriptionId.random();
        var command = new StartSolverCommand(
                configuration,
                subscriptionId,
                solverDefaultTimeLimitInSec,
                username
        );
        publisher.publishEvent(command);
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        return EndpointSubscription.of(
                this.sink.asFlux().takeUntil(s -> s.getSolutionStatus() == SolutionStatus.STOPPED),
                () -> publisher.publishEvent(new StopSolverCommand(subscriptionId, username))
        );
    }

    @RolesAllowed(Role.ROLE_USER)
    public @Nonnull SolverSolutionDTO getLatestSolution(String subscriptionId) {
        return solverServiceQuery.findLatestSolution(SolverSubscriptionId.from(subscriptionId))
                                 .orElseThrow(() -> new IllegalArgumentException("No solution found for " + subscriptionId));
    }

    @EventListener
    protected void on(SolutionHasBeenFound event) {
        log.debug("SolutionHasBeenFound event");
        if (this.sink != null) {
            sink.tryEmitNext(
                    new SolutionNotificationDTO(event.solution().getSolutionStatus(), event.subscriptionId().id())
            );
        }
    }

    @EventListener
    protected void on(SolverHasBeenStopped event) {
        log.debug("SolverHasBeenStopped event");
        if (this.sink != null) {
            sink.tryEmitNext(
                    new SolutionNotificationDTO(SolutionStatus.STOPPED, event.subscriptionId().id())
            );
        }
    }
}
