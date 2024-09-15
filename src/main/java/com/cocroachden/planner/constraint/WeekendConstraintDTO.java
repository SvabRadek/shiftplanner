package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.constraints.specific.weekends.request.WeekendConstraint;
import dev.hilla.Nonnull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeekendConstraintDTO implements ConstraintDTO {
  public static WeekendConstraintDTO from(String id, WeekendConstraint request) {
    return new WeekendConstraintDTO(
        id,
        request.getOwner().getId(),
        request.getAssignOnlyFullWorkingWeekends(),
        request.getFullWorkWeekendPenalty()
    );
  }

  @Nonnull
  private final ConstraintType type = ConstraintType.WEEKEND_CONSTRAINT;
  @Nonnull
  @Setter
  private String id;
  @Nonnull
  private String owner;
  @Nonnull
  private Boolean assignOnlyFullWorkingWeekends;
  @Nonnull
  private Integer penaltyForNotFullWorkingWeekend;
}
