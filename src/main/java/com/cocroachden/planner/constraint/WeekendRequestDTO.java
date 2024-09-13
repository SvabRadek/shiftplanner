package com.cocroachden.planner.constraint;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.weekends.request.WeekendRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeekendRequestDTO implements ConstraintRequestDTO {
  public static WeekendRequestDTO from(String id, WeekendRequest request) {
    return new WeekendRequestDTO(
        id,
        ConstraintType.WEEKEND_REQUEST,
        request.getOwner().orElse(null),
        request.getAssignOnlyFullWorkingWeekends(),
        request.getFullWorkWeekendPenalty()
    );
  }

  @Nonnull
  private String id;
  @Nonnull
  private ConstraintType type;
  @Nonnull
  private EmployeeId owner;
  @Nonnull
  private Boolean assignOnlyFullWorkingWeekends;
  @Nonnull
  private Integer penaltyForNotFullWorkingWeekend;
}
