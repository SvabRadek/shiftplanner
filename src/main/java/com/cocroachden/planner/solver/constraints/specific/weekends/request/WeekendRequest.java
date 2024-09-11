package com.cocroachden.planner.solver.constraints.specific.weekends.request;

import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.constraint.api.WeekendRequestDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("WeekendRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeekendRequest extends AbstractEmployeeSpecificConstraint {
  public static ConstraintType TYPE_ID = ConstraintType.WEEKEND_REQUEST;

  public static WeekendRequest from(WeekendRequestDTO dto) {
    return new WeekendRequest(
        dto.getOwner(),
        dto.getAssignOnlyFullWorkingWeekends(),
        dto.getPenaltyForNotFullWorkingWeekend()
    );
  }

  private Boolean assignOnlyFullWorkingWeekends;
  private Integer fullWorkWeekendPenalty = 0;

  public WeekendRequest(
      EmployeeId owner,
      Boolean assignOnlyFullWorkingWeekends,
      Integer fullWorkWeekendPenalty
  ) {
    super(TYPE_ID, owner);
    this.assignOnlyFullWorkingWeekends = assignOnlyFullWorkingWeekends;
    this.fullWorkWeekendPenalty = fullWorkWeekendPenalty;
  }
}
