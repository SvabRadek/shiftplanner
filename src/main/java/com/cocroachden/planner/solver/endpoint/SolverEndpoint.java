package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.solver.SolverConfigurationId;
import com.cocroachden.planner.solver.api.SolutionStatus;
import com.cocroachden.planner.solver.api.SolverSolutionDTO;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import com.cocroachden.planner.solver.service.SolverService;
import com.cocroachden.planner.solver.service.SolverConfiguration;
import com.cocroachden.planner.solver.service.schedule.ScheduleEmployee;
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
public class SolverEndpoint {
  private final SolverService solverService;
  private final SolverConfigurationRepository solverConfigurationRepository;

  @Transactional
  public EndpointSubscription<@Nonnull SolverSolutionDTO> solve(String configurationId) {
    log.debug("Solving configuration: {}", configurationId);
    var solverConfigRecord = solverConfigurationRepository.getById(new SolverConfigurationId(configurationId));
    var solverConfig = new SolverConfiguration(
        solverConfigRecord.getStartDate(),
        solverConfigRecord.getEndDate(),
        solverConfigRecord.getEmployeeAssignments().stream()
            .map(ScheduleEmployee::from)
            .toList(),
        solverConfigRecord.getConstraintRequestRecords().stream()
            .map(ConstraintRequestRecord::getRequest)
            .toList()
    );
    var flux = Flux
        .<SolverSolutionDTO>create(fluxSink -> {
          solverService.solve(solverConfig, fluxSink::next);
        }).takeWhile(solution -> solution.getSolutionStatus().equals(SolutionStatus.OK));
    return EndpointSubscription.of(
        flux,
        () -> {
          solverService.stop();
          log.info("Subscription disconnected!");
        }
    );
  }

  public void stop() {
    this.solverService.stop();
  }
}
