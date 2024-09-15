package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraint;
import dev.hilla.Nonnull;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShiftPatternConstraintDTO implements ConstraintDTO {
  public static ShiftPatternConstraintDTO from(String id, ShiftPatternConstraint request) {
    return new ShiftPatternConstraintDTO(
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
  @Setter
  private String id;
  @Nonnull
  private String owner;
  @Nonnull
  private List<@Nonnull WorkShifts> shiftPattern;
  @Nonnull
  private Integer reward;
  @Nonnull
  private Integer startDayIndex;
}
