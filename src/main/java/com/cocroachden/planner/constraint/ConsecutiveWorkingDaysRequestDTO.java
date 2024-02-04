package com.cocroachden.planner.constraint;

import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConsecutiveWorkingDaysRequestDTO {
  public static ConsecutiveWorkingDaysRequestDTO from(ConsecutiveWorkingDaysRequest request) {
    return new ConsecutiveWorkingDaysRequestDTO(
        ConsecutiveWorkingDaysRequest.CONSTRAINT_TYPE,
        request.getHardMin(),
        request.getSoftMin(),
        request.getMinPenalty(),
        request.getSoftMax(),
        request.getMaxPenalty(),
        request.getHardMax()
    );
  }
  @Nonnull
  private ConstraintType type;
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
