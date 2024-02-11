package com.cocroachden.planner.constraint;


import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShiftsPerScheduleRequestDTO {
  public static ShiftsPerScheduleRequestDTO from(UUID id, ShiftsPerScheduleRequest request) {
    return new ShiftsPerScheduleRequestDTO(
        id,
        request.getOwner().orElseThrow(),
        request.getType(),
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
  private UUID id;
  @Nonnull
  private WorkerId owner;
  @Nonnull
  private ConstraintType type;
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
