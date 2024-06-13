package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.solver.api.SolutionStatus;
import com.cocroachden.planner.solver.api.SolverSolutionDTO;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import com.cocroachden.planner.solver.solver.Solver;
import com.cocroachden.planner.solver.solver.SolverConfiguration;
import com.cocroachden.planner.solver.solver.schedule.ScheduleWorker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.UUID;

@BrowserCallable
@AnonymousAllowed
@Slf4j
@AllArgsConstructor
public class PlannerEndpoint {
  private final Solver solver;
  private final SolverConfigurationRepository plannerConfigurationRepository;
  private final ConstraintRequestRepository constraintRequestRepository;

  @Transactional
  public EndpointSubscription<@Nonnull SolverSolutionDTO> solve(UUID configurationId) {
    log.debug("Solving configuration: {}", configurationId.toString());
    var solverConfigRecord = plannerConfigurationRepository.getById(configurationId);
    var solverConfig = new SolverConfiguration(
        solverConfigRecord.getStartDate(),
        solverConfigRecord.getEndDate(),
        solverConfigRecord.getWorkers().stream()
            .map(workerId -> new ScheduleWorker(workerId, 1))
            .toList(),
        solverConfigRecord.getConstraintRequestRecords().stream()
            .map(ConstraintRequestRecord::getRequest)
            .toList()
    );
    var flux = Flux
        .<SolverSolutionDTO>create(fluxSink -> solver.solve(solverConfig, fluxSink::next))
        .takeWhile(solution -> solution.getSolutionStatus().equals(SolutionStatus.OK));

    return EndpointSubscription.of(
        flux,
        () -> {
          solver.stop();
          log.info("Subscription disconnected!");
        }
    );
  }

  public void stop() {
    this.solver.stop();
  }
}
