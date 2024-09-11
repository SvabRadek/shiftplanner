package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.api.WorkShifts;
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
        ShiftPatternConstraintRequest.ID,
        request.getOwner().orElse(null),
        Arrays.stream(request.getShiftPattern()).toList(),
        request.getReward(),
        request.getStartDateIndex()
    );
  }

  @Nonnull
  private String id;
  @Nonnull
  private ConstraintType type;
  private EmployeeId owner;
  @Nonnull
  private List<@Nonnull WorkShifts> shiftPattern;
  @Nonnull
  private Integer reward;
  @Nonnull
  private Integer startDayIndex;
}
