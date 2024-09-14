package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.constraints.specific.weekends.request.WeekendRequest;
import dev.hilla.Nonnull;
import lombok.*;
import org.checkerframework.common.value.qual.StringVal;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WeekendRequestDTO implements ConstraintRequestDTO {
  public static WeekendRequestDTO from(String id, WeekendRequest request) {
    return new WeekendRequestDTO(
        id,
        request.getOwner().getId(),
        request.getAssignOnlyFullWorkingWeekends(),
        request.getFullWorkWeekendPenalty()
    );
  }

  @Nonnull
  private final ConstraintType type = ConstraintType.WEEKEND_REQUEST;
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
