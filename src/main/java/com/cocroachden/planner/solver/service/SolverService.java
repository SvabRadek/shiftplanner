package com.cocroachden.planner.solver.service;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.command.solveconfiguration.SolutionHasBeenFound;
import com.cocroachden.planner.solver.command.solveconfiguration.SolverHasBeenStarted;
import com.cocroachden.planner.solver.command.solveconfiguration.StartSolverCommand;
import com.cocroachden.planner.solver.command.stopsolver.SolverHasBeenStopped;
import com.cocroachden.planner.solver.command.stopsolver.StopSolverCommand;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SolverService {

    private final SolverConfigurationRepository solverConfigurationRepository;
    private final ApplicationEventPublisher publisher;
    private final Solver solver;

    @EventListener
    @Transactional
    public SolverHasBeenStarted handle(StartSolverCommand command) {
        var configuration = solverConfigurationRepository.getById(command.configurationId().getId());
        solver.solve(
                SolverProblemConfiguration.from(configuration),
                solution -> publisher.publishEvent(
                        new SolutionHasBeenFound(command.subscriptionId(), solution)
                ),
                SolverOptions.builder()
                        .solvingLimitInSec(command.limitInSec())
                        .build()
        );
        return new SolverHasBeenStarted(command.subscriptionId());
    }

    @EventListener
    public SolverHasBeenStopped handle(StopSolverCommand command) {
        solver.stop();
        return new SolverHasBeenStopped(command.subscriptionId());
    }

}
