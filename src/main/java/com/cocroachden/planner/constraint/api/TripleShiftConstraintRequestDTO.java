package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TripleShiftConstraintRequestDTO implements ConstraintRequestDTO {
  public static TripleShiftConstraintRequestDTO from(UUID id, TripleShiftConstraintRequest request) {
    return new TripleShiftConstraintRequestDTO(
        id,
        ConstraintType.TRIPLE_SHIFTS_CONSTRAINT,
        request.getOwner().orElse(null),
        request.getPenaltyForShiftTripletOutsideWeekend()
    );
  }

  @Nonnull
  private UUID id;
  @Nonnull
  private ConstraintType type;
  @Nonnull
  private EmployeeId owner;
  @Nonnull
  private Integer penaltyForShiftTripletOutsideWeekend;
}
