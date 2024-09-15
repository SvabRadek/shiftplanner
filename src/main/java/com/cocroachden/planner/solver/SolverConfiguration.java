package com.cocroachden.planner.solver;


import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.service.schedule.ScheduleEmployee;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

public record SolverConfiguration(
    LocalDate startDate,
    LocalDate endDate,
    List<ScheduleEmployee> scheduleEmployees,
    List<SolverConstraint> solverConstraints
) {
  @Override
  public String toString() {
    return "{ startDate: %s, endDate: %s, scheduleEmployees: [%s] }".formatted(
        startDate.toString(),
        endDate.toString(),
        StringUtils.join(scheduleEmployees.stream().map(w -> w.employeeId().toString()).toList(), ", ")
    );
  }
}
