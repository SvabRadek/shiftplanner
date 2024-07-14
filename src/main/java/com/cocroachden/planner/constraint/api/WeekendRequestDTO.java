package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.weekends.request.WeekendRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeekendRequestDTO implements ConstraintRequestDTO {
  public static WeekendRequestDTO from(UUID id, WeekendRequest request) {
    return new WeekendRequestDTO(
        id,
        ConstraintType.WEEKEND_REQUEST,
        request.getOwner().orElse(null),
        request.getAssignOnlyFullWorkingWeekends(),
        request.getFullWorkWeekendPenalty()
    );
  }

  @Nonnull
  private UUID id;
  @Nonnull
  private ConstraintType type;
  @Nonnull
  private EmployeeId owner;
  @Nonnull
  private Boolean assignOnlyFullWorkingWeekends;
  @Nonnull
  private Integer penaltyForNotFullWorkingWeekend;
}
