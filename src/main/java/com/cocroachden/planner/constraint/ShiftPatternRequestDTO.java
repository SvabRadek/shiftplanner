package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShiftPatternRequestDTO implements ConstraintRequestDTO {
  public static ShiftPatternRequestDTO from(String id, ShiftPatternConstraintRequest request) {
    return new ShiftPatternRequestDTO(
        id,
        request.getOwner().getId(),
        Arrays.stream(request.getShiftPattern()).toList(),
        request.getReward(),
        request.getStartDateIndex()
    );
  }

  @Nonnull
  private final ConstraintType type = ConstraintType.SHIFT_PATTERN_CONSTRAINT;
  @Nonnull
  private String id;
  private String owner;
  @Nonnull
  private List<@Nonnull WorkShifts> shiftPattern;
  @Nonnull
  private Integer reward;
  @Nonnull
  private Integer startDayIndex;
}
