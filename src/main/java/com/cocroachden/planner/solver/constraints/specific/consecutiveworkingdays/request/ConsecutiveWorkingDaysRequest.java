package com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request;


import com.cocroachden.planner.constraint.api.ConsecutiveWorkingDaysRequestDTO;
import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ConsecutiveWorkingDaysRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsecutiveWorkingDaysRequest extends AbstractMinMaxRequest {
  public static final ConstraintType CONSTRAINT_TYPE = ConstraintType.CONSECUTIVE_WORKING_DAYS;

  public static ConsecutiveWorkingDaysRequest from(ConsecutiveWorkingDaysRequestDTO dto) {
    return new ConsecutiveWorkingDaysRequest(
        dto.getTargetShift(),
        dto.getHardMin(),
        dto.getSoftMin(),
        dto.getMinPenalty(),
        dto.getSoftMax(),
        dto.getMaxPenalty(),
        dto.getHardMax()
    );
  }

  private WorkShifts targetShift;

  public ConsecutiveWorkingDaysRequest(
      EmployeeId owner,
      WorkShifts targetShift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(CONSTRAINT_TYPE, owner, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
    this.targetShift = targetShift;
  }

  public ConsecutiveWorkingDaysRequest(
      WorkShifts targetShift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax) {
    this(null, targetShift, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
  }

  @Override
  public String toString() {
    return "{ " +
        "type: " + this.getType().toString() +
        ", targetShift: " + targetShift.getSymbol() +
        ", softMin: " + this.getSoftMin() +
        ", minPenalty: " + this.getMinPenalty() +
        ", hardMin: " + this.getHardMin() +
        ", softMax: " + this.getSoftMax() +
        ", maxPenalty: " + this.getMaxPenalty() +
        ", hardMax: " + this.getHardMax() +
        ", owner: " + (this.getOwner().isPresent() ? this.getOwner().get() : "none") +
        " }";
  }
}

