package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.security.Role;
import com.cocroachden.planner.solver.SolutionStatus;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.command.solveconfiguration.SolutionHasBeenFound;
import com.cocroachden.planner.solver.command.solveconfiguration.StartSolverCommand;
import com.cocroachden.planner.solver.command.stopsolver.StopSolverCommand;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
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

    @Value("${application.solver.default-solver-time-limit-in-sec}")
    private final Integer solverDefaultTimeLimitInSec = 60;

    private final ApplicationEventPublisher publisher;
    private final Flux<SolverSolutionDTO> solutionFlux;
    private final Sinks.Many<SolverSolutionDTO> sink;

    public SolverEndpoint(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        this.sink = Sinks.many().unicast().onBackpressureBuffer();
        this.solutionFlux = sink.asFlux().takeWhile(solution -> solution.getSolutionStatus().equals(SolutionStatus.OK));
    }

    public EndpointSubscription<@Nonnull SolverSolutionDTO> solveProblem(String configurationId) {
        SolverSubscriptionId subscriptionId = SolverSubscriptionId.random();
        var command = new StartSolverCommand(
                new SolverConfigurationId(configurationId),
                subscriptionId,
                solverDefaultTimeLimitInSec
        );
        publisher.publishEvent(command);
        return EndpointSubscription.of(
                solutionFlux,
                () -> publisher.publishEvent(new StopSolverCommand(subscriptionId))
        );
    }

    public void stop() {
        this.publisher.publishEvent(
                new StopSolverCommand(new SolverSubscriptionId("to be implemented"))
        );
    }

    @EventListener
    protected void on(SolutionHasBeenFound event) {
        sink.tryEmitNext(event.solution());
    }
}
