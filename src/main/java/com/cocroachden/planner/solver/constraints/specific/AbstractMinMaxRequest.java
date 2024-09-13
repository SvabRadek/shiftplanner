package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("AbstractMinMaxRequest")
public class AbstractMinMaxRequest extends AbstractEmployeeSpecificConstraint {
  private Integer softMin;
  private Integer minPenalty;
  private Integer hardMin;
  private Integer softMax;
  private Integer maxPenalty;
  private Integer hardMax;
  public AbstractMinMaxRequest(
      ConstraintType constraintType,
      EmployeeId owner,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(constraintType, owner);
    this.softMin = softMin;
    this.minPenalty = minPenalty;
    this.hardMin = hardMin;
    this.softMax = softMax;
    this.maxPenalty = maxPenalty;
    this.hardMax = hardMax;
  }
}
