package com.cocroachden.planner.solver.service;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.command.solveconfiguration.ConfigurationHasBeenSolved;
import com.cocroachden.planner.solver.command.solveconfiguration.SolveConfigurationCommand;
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
    public void handle(SolveConfigurationCommand command) {
        var configuration = solverConfigurationRepository.getById(command.configurationId().getId());
        solver.solve(
                SolverProblemConfiguration.from(configuration),
                solution -> publisher.publishEvent(
                        new ConfigurationHasBeenSolved(
                                command.subscriptionId(),
                                solution
                        )
                ),
                SolverOptions.builder()
                        .solvingLimitInSec(command.limitInSec())
                        .build()
        );
    }

}
