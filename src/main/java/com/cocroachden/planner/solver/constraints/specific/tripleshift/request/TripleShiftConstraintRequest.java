package com.cocroachden.planner.solver.constraints.specific.tripleshift.request;

import com.cocroachden.planner.constraint.api.TripleShiftConstraintRequestDTO;
import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

@Getter
@JsonTypeName("TripleShiftConstraintRequest")
public class TripleShiftConstraintRequest extends AbstractEmployeeSpecificConstraint {
  public static ConstraintType TYPE = ConstraintType.TRIPLE_SHIFTS_CONSTRAINT;
  private Integer penaltyForShiftTripletOutsideWeekend;

  protected TripleShiftConstraintRequest() {
    super();
  }

  public static TripleShiftConstraintRequest from(TripleShiftConstraintRequestDTO requestDTO) {
    return new TripleShiftConstraintRequest(
        requestDTO.getOwner(),
        requestDTO.getPenaltyForShiftTripletOutsideWeekend()
    );
  }

  public TripleShiftConstraintRequest(
      EmployeeId owner,
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
