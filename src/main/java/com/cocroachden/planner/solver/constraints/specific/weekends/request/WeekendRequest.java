package com.cocroachden.planner.solver.constraints.specific.weekends.request;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.WeekendRequestDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("WeekendRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WeekendRequest implements EmployeeConstraint {
  public static WeekendRequest from(WeekendRequestDTO dto) {
    return new WeekendRequest(
            EmployeeId.from(dto.getOwner()),
            dto.getAssignOnlyFullWorkingWeekends(),
            dto.getPenaltyForNotFullWorkingWeekend()
    );
  }

  private final ConstraintType type = ConstraintType.WEEKEND_REQUEST;
  private EmployeeId owner;
  private Boolean assignOnlyFullWorkingWeekends;
  private Integer fullWorkWeekendPenalty;
}
