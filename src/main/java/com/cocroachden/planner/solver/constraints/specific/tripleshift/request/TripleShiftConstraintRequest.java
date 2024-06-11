package com.cocroachden.planner.solver.constraints.specific.tripleshift.request;

import com.cocroachden.planner.constraint.api.TripleShiftConstraintRequestDTO;
import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TripleShiftConstraintRequest extends AbstractConstraintRequest {
  public static ConstraintType TYPE = ConstraintType.TRIPLE_SHIFTS_CONSTRAINT;
  private Integer penaltyForShiftTripletOutsideWeekend;

  public static TripleShiftConstraintRequest from(TripleShiftConstraintRequestDTO requestDTO) {
    return new TripleShiftConstraintRequest(
        requestDTO.getOwner(),
        requestDTO.getPenaltyForShiftTripletOutsideWeekend()
    );
  }

  public TripleShiftConstraintRequest(
      WorkerId owner,
      Integer penaltyForShiftTripletOutsideWeekend
  ) {
    super(ConstraintType.TRIPLE_SHIFTS_CONSTRAINT, owner);
    this.penaltyForShiftTripletOutsideWeekend = penaltyForShiftTripletOutsideWeekend;
  }

  @Override
  public String toString() {
    return "{ type: %s, owner: %s, penaltyForShiftTripletOutsideWeekend: %s }"
        .formatted(
            this.getType(),
            this.getOwner().orElse(null),
            this.penaltyForShiftTripletOutsideWeekend
        );
  }
}
