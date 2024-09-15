package com.cocroachden.planner.solver.constraints.specific.weekends.request;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.WeekendConstraintDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.EmployeeSolverConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("WeekendConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WeekendConstraint implements EmployeeSolverConstraint {
  public static WeekendConstraint from(WeekendConstraintDTO dto) {
    return new WeekendConstraint(
            EmployeeId.from(dto.getOwner()),
            dto.getAssignOnlyFullWorkingWeekends(),
            dto.getPenaltyForNotFullWorkingWeekend()
    );
  }

  private final ConstraintType type = ConstraintType.WEEKEND_CONSTRAINT;
  private EmployeeId owner;
  private Boolean assignOnlyFullWorkingWeekends;
  private Integer fullWorkWeekendPenalty;
}
