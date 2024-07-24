package com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request;

import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("EvenShiftDistributionRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvenShiftDistributionRequest extends AbstractEmployeeSpecificConstraint {
  public static ConstraintType TYPE_ID = ConstraintType.EVEN_SHIFT_DISTRIBUTION;


  public EvenShiftDistributionRequest(EmployeeId owner) {
    super(TYPE_ID, owner);
  }
}
