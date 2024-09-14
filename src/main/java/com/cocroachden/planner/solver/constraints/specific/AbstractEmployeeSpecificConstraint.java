package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("AbstractEmployeeSpecificConstraint")
public abstract class AbstractEmployeeSpecificConstraint implements ConstraintRequest {
  private ConstraintType type;
  @Getter
  private EmployeeId owner;

  @Override
  public ConstraintType getType() {
    return this.type;
  }
}
