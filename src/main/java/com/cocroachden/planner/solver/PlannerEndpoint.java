package com.cocroachden.planner.solver;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.plannerconfiguration.repository.ConfigurationRequestLink;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRepository;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayRequest;
import com.cocroachden.planner.solver.schedule.ScheduleWorker;
import com.cocroachden.planner.solver.solver.ScheduleSolver;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.EndpointSubscription;
import dev.hilla.Nonnull;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@Slf4j
@AllArgsConstructor
public class PlannerEndpoint {
  private final ScheduleSolver scheduleSolver;
  private final PlannerConfigurationRepository plannerConfigurationRepository;
  private final ConstraintRequestRepository constraintRequestRepository;

  public void stop() {
    this.scheduleSolver.stop();
  }

  @Transactional
  public EndpointSubscription<@Nonnull ScheduleResultDTO> solve(UUID configurationId) {
    log.debug("Solving configuration: {}", configurationId.toString());
    var config = plannerConfigurationRepository.getById(configurationId);
    var plannerConfig = new SchedulePlanConfiguration(
        config.getStartDate(),
        config.getEndDate(),
        config.getWorkers().stream()
            .map(workerId -> new ScheduleWorker(workerId, 1))
            .toList()
    );
    log.debug("Planner Configuration: {}", plannerConfig);
    var constraints = StreamSupport.stream(
            constraintRequestRepository.findAllById(
                config.getConstraintRequestInstances().stream()
                    .map(ConfigurationRequestLink::getRequestId)
                    .toList()
            ).spliterator(), false)
        .map(ConstraintRequestRecord::getRequest)
        .collect(Collectors.toList());
    if (constraints.stream().noneMatch(r -> r instanceof OneShiftPerDayRequest)) {
      constraints.add(new OneShiftPerDayRequest());
    }
    log.debug(
        "Constraints: [\n{}\n]",
        StringUtils.join(constraints.stream().map(ConstraintRequest::toString).toArray(), ",\n")
    );
    var flux = Flux.<ScheduleResultDTO>create(fluxSink ->
        scheduleSolver.solve(
            fluxSink::next,
            plannerConfig,
            constraints
        ));

    return EndpointSubscription.of(
        flux,
        () -> {
          scheduleSolver.stop();
          log.info("Disconnected!");
        }
    );
  }
}
