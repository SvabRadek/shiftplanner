package com.cocroachden.planner.solver;


import com.cocroachden.planner.constraint.mapping.ConstraintMapper;
import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.solver.schedule.ScheduleEmployee;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

public record SolverProblemConfiguration(
    LocalDate startDate,
    LocalDate endDate,
    List<ScheduleEmployee> scheduleEmployees,
    List<SolverConstraint> solverConstraints
) {

  public static SolverProblemConfiguration from(SolverConfigurationRecord record) {
    return new SolverProblemConfiguration(
            record.getStartDate(),
            record.getEndDate(),
            record.getEmployeeAssignments().stream()
                    .map(ScheduleEmployee::from)
                    .toList(),
            record.getConstraintRecords().stream()
                    .map(ConstraintRecord::getRequest)
                    .toList()
    );
  }

  public static SolverProblemConfiguration from(SolverConfigurationDTO dto) {
    return new SolverProblemConfiguration(
            dto.getStartDate(),
            dto.getEndDate(),
            dto.getEmployees().stream()
                    .map(a -> new ScheduleEmployee(a.getEmployeeId(), a.getWeight()))
                    .toList(),
            dto.getConstraints().stream()
                    .map(ConstraintMapper::fromDto)
                    .toList()
    );
  }


  @Override
  public String toString() {
    return "{ startDate: %s, endDate: %s, scheduleEmployees: [%s] }".formatted(
        startDate.toString(),
        endDate.toString(),
        StringUtils.join(scheduleEmployees.stream().map(w -> w.employeeId().toString()).toList(), ", ")
    );
  }
}
