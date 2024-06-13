package com.cocroachden.planner.solver.service;


import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.service.schedule.ScheduleEmployee;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

public record SolverConfiguration(
    LocalDate startDate,
    LocalDate endDate,
    List<ScheduleEmployee> scheduleEmployees,
    List<ConstraintRequest> constraintRequests
) {
  @Override
  public String toString() {
    return "{ startDate: %s, endDate: %s, scheduleWorkers: [%s] }".formatted(
        startDate.toString(),
        endDate.toString(),
        StringUtils.join(scheduleEmployees.stream().map(w -> w.employeeId().toString()).toList(), ", ")
    );
  }
}
