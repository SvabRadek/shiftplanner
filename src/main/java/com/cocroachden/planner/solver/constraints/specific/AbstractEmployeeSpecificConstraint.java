package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("AbstractEmployeeSpecificConstraint")
public abstract class AbstractEmployeeSpecificConstraint implements ConstraintRequest {
  private ConstraintType type;
  private EmployeeId owner;

  @Override
  public ConstraintType getType() {
    return this.type;
  }

  public Optional<EmployeeId> getOwner() {
    return Optional.ofNullable(this.owner);
  }
}
