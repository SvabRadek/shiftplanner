package com.cocroachden.planner.solver;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.service.ConstraintRequestService;
import com.cocroachden.planner.plannerconfiguration.repository.ConfigurationRequestLink;
import com.cocroachden.planner.plannerconfiguration.service.PlannerConfigurationService;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayRequest;
import com.cocroachden.planner.solver.schedule.ScheduleWorker;
import com.cocroachden.planner.solver.solver.ScheduleSolutionCb;
import com.cocroachden.planner.solver.solver.ScheduleSolver;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.stream.Collectors;

@BrowserCallable
@AnonymousAllowed
@Slf4j
@AllArgsConstructor
public class PlannerEndpoint {
  private final ScheduleSolver scheduleSolver;
  private final PlannerConfigurationService plannerConfigurationService;
  private final ConstraintRequestService constraintRequestService;

  public void stop() {
    this.scheduleSolver.stop();
  }

  @Transactional
  public EndpointSubscription<@Nonnull ScheduleResultDTO> solve(
      UUID configurationId,
      Integer timeLimitInSec
  ) {
    var config = plannerConfigurationService.getConfiguration(configurationId);
    var plannerConfig = new SchedulePlanConfiguration(
        config.getStartDate(),
        config.getEndDate(),
        config.getWorkers().stream()
            .map(workerId -> new ScheduleWorker(workerId, 1))
            .toList()
    );
    var constraints = constraintRequestService.getRecords(
            config.getConstraintRequestInstances().stream()
                .map(ConfigurationRequestLink::getRequestId)
                .toList()
        ).stream()
        .map(ConstraintRequestRecord::getRequest)
        .collect(Collectors.toList());
    if (constraints.stream().noneMatch(r -> r instanceof OneShiftPerDayRequest)) {
      constraints.add(new OneShiftPerDayRequest());
    }

    var flux = Flux.<ScheduleResultDTO>create(fluxSink -> {
      scheduleSolver.solve(
          fluxSink,
          plannerConfig,
          constraints,
          timeLimitInSec
      );
    });

    return EndpointSubscription.of(
        flux,
        () -> {
          scheduleSolver.stop();
          log.info("Disconnected!");
        }
    );
  }
}
