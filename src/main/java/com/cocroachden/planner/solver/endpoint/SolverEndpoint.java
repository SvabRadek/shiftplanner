package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.solver.SolutionStatus;
import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.service.Solver;
import com.cocroachden.planner.solver.service.SolverOptions;
import com.cocroachden.planner.solver.service.ScheduleSolver;
import com.cocroachden.planner.solver.service.schedule.ScheduleEmployee;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
@Slf4j
@AllArgsConstructor
public class SolverEndpoint {
    private final Solver scheduleSolver;
    private final SolverConfigurationRepository solverConfigurationRepository;

    @Transactional
    public EndpointSubscription<@Nonnull SolverSolutionDTO> solve(String configurationId) {
        log.debug("Solving configuration: {}", configurationId);
        var solverConfigRecord = solverConfigurationRepository.getById(configurationId);
        var solverConfig = new SolverProblemConfiguration(
                solverConfigRecord.getStartDate(),
                solverConfigRecord.getEndDate(),
                solverConfigRecord.getEmployeeAssignments().stream()
                        .map(ScheduleEmployee::from)
                        .toList(),
                solverConfigRecord.getConstraintRecords().stream()
                        .map(ConstraintRecord::getRequest)
                        .toList()
        );
        var flux = Flux
                .<SolverSolutionDTO>create(fluxSink -> {
                    scheduleSolver.solve(solverConfig, fluxSink::next, SolverOptions.builder().build());
                }).takeWhile(solution -> solution.getSolutionStatus().equals(SolutionStatus.OK));
        return EndpointSubscription.of(
                flux,
                () -> {
                    scheduleSolver.stop();
                    log.info("Subscription disconnected!");
                }
        );
    }

    public void stop() {
        this.scheduleSolver.stop();
    }
}
