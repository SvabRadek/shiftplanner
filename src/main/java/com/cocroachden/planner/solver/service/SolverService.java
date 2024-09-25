package com.cocroachden.planner.solver.service;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.command.solveconfiguration.SolutionHasBeenFound;
import com.cocroachden.planner.solver.command.solveconfiguration.SolverHasBeenStarted;
import com.cocroachden.planner.solver.command.solveconfiguration.StartSolverCommand;
import com.cocroachden.planner.solver.command.stopsolver.SolverHasBeenStopped;
import com.cocroachden.planner.solver.command.stopsolver.StopSolverCommand;
import com.cocroachden.planner.solver.service.solver.Solver;
import com.cocroachden.planner.solver.service.solver.SolverFactory;
import com.cocroachden.planner.solver.service.solver.SolverOptions;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class SolverService {

    private final SolverConfigurationRepository solverConfigurationRepository;
    private final ApplicationEventPublisher publisher;
    private final SolverFactory solverFactory;
    @Getter
    private final ConcurrentHashMap<SolverSubscriptionId, Solver> subscriptions = new ConcurrentHashMap<>();

    @Transactional
    @EventListener
    public void handle(StartSolverCommand command) {
        log.debug("Handling StartSolverCommand...");
        var configuration = SolverProblemConfiguration.from(
                solverConfigurationRepository.getById(command.configurationId().getId())
        );
        publisher.publishEvent(new SolverHasBeenStarted(command.subscriptionId()));
        CompletableFuture.runAsync(() -> {
                    var solver = solverFactory.instantiate();
                    subscriptions.put(command.subscriptionId(), solver);
                    solver.solve(
                            configuration,
                            solution -> publisher.publishEvent(new SolutionHasBeenFound(command.subscriptionId(), solution)),
                            SolverOptions.builder()
                                    .solvingLimitInSec(command.limitInSec())
                                    .build()
                    );
                }).orTimeout(command.limitInSec() + 1, TimeUnit.SECONDS)
                .whenComplete((aVoid, throwable) -> {
                    subscriptions.remove(command.subscriptionId());
                    publisher.publishEvent(new SolverHasBeenStopped(command.subscriptionId()));
                });
    }

    @EventListener
    public void handle(StopSolverCommand command) {
        log.debug("Handling StopSolverCommand...");
        if (subscriptions.containsKey(command.subscriptionId())) {
            subscriptions.get(command.subscriptionId()).stop();
        }
        //SolverHasBeenStopped event is dispatched on thread cancellation
    }
}
