package com.cocroachden.planner.solver.solver;


import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.solver.schedule.ScheduleWorker;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

public record SolverConfiguration(
    LocalDate startDate,
    LocalDate endDate,
    List<ScheduleWorker> scheduleWorkers,
    List<ConstraintRequest> constraintRequests
) {
  @Override
  public String toString() {
    return "{ startDate: %s, endDate: %s, scheduleWorkers: [%s] }".formatted(
        startDate.toString(),
        endDate.toString(),
        StringUtils.join(scheduleWorkers.stream().map(w -> w.workerId().toString()).toList(), ", ")
    );
  }
}
