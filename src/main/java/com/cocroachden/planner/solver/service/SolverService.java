package com.cocroachden.planner.solver.service;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.SolverTask;
import com.cocroachden.planner.solver.command.solveconfiguration.SolutionHasBeenFound;
import com.cocroachden.planner.solver.command.solveconfiguration.SolverHasBeenStarted;
import com.cocroachden.planner.solver.command.solveconfiguration.StartSolverCommand;
import com.cocroachden.planner.solver.command.stopsolver.SolverHasBeenStopped;
import com.cocroachden.planner.solver.command.stopsolver.StopSolverCommand;
import com.cocroachden.planner.solver.solver.SolverFactory;
import com.cocroachden.planner.solver.solver.SolverOptions;
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

    private final ApplicationEventPublisher publisher;
    private final SolverFactory solverFactory;
    @Getter
    private final ConcurrentHashMap<SolverSubscriptionId, SolverTask> subscriptions = new ConcurrentHashMap<>();

    @EventListener
    public void handle(StartSolverCommand command) {
        log.debug("Handling StartSolverCommand...");
        var configuration = SolverProblemConfiguration.from(command.solverConfiguration());
        var subscriptionId = command.subscriptionId();
        CompletableFuture.runAsync(() -> {
                    publisher.publishEvent(new SolverHasBeenStarted(subscriptionId));
                    var solver = solverFactory.instantiate();
                    subscriptions.put(subscriptionId, new SolverTask(subscriptionId, solver, command.username()));
                    solver.start(
                            configuration,
                            solution -> publisher.publishEvent(new SolutionHasBeenFound(subscriptionId, solution)),
                            SolverOptions.builder()
                                    .solvingLimitInSec(command.limitInSec())
                                    .build()
                    );
                }).orTimeout(command.limitInSec() + 1, TimeUnit.SECONDS)
                .whenComplete((aVoid, throwable) -> {
                    subscriptions.remove(subscriptionId);
                    publisher.publishEvent(new SolverHasBeenStopped(subscriptionId));
                });
    }

    @EventListener
    public void handle(StopSolverCommand command) {
        log.debug("Handling StopSolverCommand...");
        if (subscriptions.containsKey(command.subscriptionId())) {
            subscriptions.get(command.subscriptionId()).solver().stop();
        }
        //SolverHasBeenStopped event is dispatched on thread cancellation
    }
}
