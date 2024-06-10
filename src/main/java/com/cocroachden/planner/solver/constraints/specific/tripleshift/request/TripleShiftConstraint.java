package com.cocroachden.planner.solver.constraints.specific.tripleshift.request;

import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TripleShiftConstraint extends AbstractConstraintRequest {

  private Integer penaltyForShiftTripletOutsideWeekend;

  public TripleShiftConstraint(
      ConstraintType type,
      WorkerId owner
  ) {
    super(type, owner);
  }
}
