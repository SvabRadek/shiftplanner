package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.command.solveconfiguration.ConfigurationHasBeenSolved;
import com.cocroachden.planner.solver.command.solveconfiguration.SolveConfigurationCommand;
import com.cocroachden.planner.solver.command.stopsolver.StopSolverCommand;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@BrowserCallable
@AnonymousAllowed
@Slf4j
@RequiredArgsConstructor
public class SolverEndpoint {

    @Value("${application.solver.default-solver-time-limit-in-sec}")
    private final Integer solverDefaultTimeLimitInSec = 60;

    private final ApplicationEventPublisher publisher;
    private Flux<SolverSolutionDTO> solutionFlux;
    private FluxSink<SolverSolutionDTO> sink;

    @PostConstruct
    public void init() {
        this.solutionFlux = Flux.create(fluxSink -> this.sink = fluxSink);
    }

    @Transactional
    public EndpointSubscription<@Nonnull SolverSolutionDTO> solveProblem(String configurationId) {
        SolverSubscriptionId subscriptionId = SolverSubscriptionId.random();
        var command = new SolveConfigurationCommand(
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
    public void on(ConfigurationHasBeenSolved event) {
        sink.next(event.solution());
    }
}
