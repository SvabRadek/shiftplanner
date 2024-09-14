package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.api.WorkShifts;
import dev.hilla.Nonnull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConsecutiveWorkingDaysRequestDTO implements ConstraintRequestDTO {
  public static ConsecutiveWorkingDaysRequestDTO from(String id, ConsecutiveWorkingDaysRequest request) {
    return new ConsecutiveWorkingDaysRequestDTO(
        id,
        request.getOwner().getId(),
        request.getTargetShift(),
        request.getHardMin(),
        request.getSoftMin(),
        request.getMinPenalty(),
        request.getSoftMax(),
        request.getMaxPenalty(),
        request.getHardMax()
    );
  }
  @Nonnull
  private final ConstraintType type = ConstraintType.CONSECUTIVE_WORKING_DAYS;
  @Nonnull
  private String id;
  @Nonnull
  private String owner;
  @Nonnull
  private WorkShifts targetShift;
  @Nonnull
  private Integer hardMin;
  @Nonnull
  private Integer softMin;
  @Nonnull
  private Integer minPenalty;
  @Nonnull
  private Integer softMax;
  @Nonnull
  private Integer maxPenalty;
  @Nonnull
  private Integer hardMax;
}
