package com.cocroachden.planner.constraint;

import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShiftPatternRequestDTO implements ConstraintRequestDTO {
  public static ShiftPatternRequestDTO from(UUID id, ShiftPatternConstraintRequest request) {
    return new ShiftPatternRequestDTO(
        id,
        ShiftPatternConstraintRequest.ID,
        request.getOwner().orElse(null),
        Arrays.stream(request.getShiftPattern()).toList(),
        request.getReward(),
        0
    );
  }

  @Nonnull
  private UUID id;
  @Nonnull
  private ConstraintType type;
  private WorkerId owner;
  @Nonnull
  private List<@Nonnull WorkShifts> shiftPattern;
  @Nonnull
  private Integer reward;
  @Nonnull
  private Integer startDayIndex;
}
