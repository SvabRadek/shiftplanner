package com.cocroachden.planner.solver.constraints.specific.employeespershift.request;


import com.cocroachden.planner.constraint.EmployeesPerShiftRequestDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("EmployeesPerShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeesPerShiftRequest extends AbstractMinMaxRequest {
  public static final ConstraintType ID = ConstraintType.EMPLOYEES_PER_SHIFT;

  public static EmployeesPerShiftRequest from(EmployeesPerShiftRequestDTO dto) {
    return new EmployeesPerShiftRequest(
        dto.getTargetShift(),
        dto.getHardMin(),
        dto.getSoftMin(),
        dto.getMinPenalty(),
        dto.getSoftMax(),
        dto.getMaxPenalty(),
        dto.getHardMax()
    );
  }

  private WorkShifts shift;
  public EmployeesPerShiftRequest(
      WorkShifts shift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(ID, null, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
    this.shift = shift;
  }


  @Override
  public String toString() {
    return "{ " +
        "type: " + this.getType() +
        ", shift: " + shift +
        ", softMin: " + this.getSoftMin() +
        ", minPenalty: " + this.getMinPenalty() +
        ", hardMin: " + this.getHardMin() +
        ", softMax: " + this.getSoftMax() +
        ", maxPenalty: " + this.getMaxPenalty() +
        ", hardMax: " + this.getHardMax() +
        " }";
  }
}
