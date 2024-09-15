package com.cocroachden.planner.solver.constraints.specific.employeespershift.request;


import com.cocroachden.planner.constraint.EmployeesPerShiftConstraintDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxConstraint;
import com.cocroachden.planner.solver.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("EmployeesPerShiftConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeesPerShiftConstraint extends AbstractMinMaxConstraint {
  public static EmployeesPerShiftConstraint from(EmployeesPerShiftConstraintDTO dto) {
    return new EmployeesPerShiftConstraint(
        dto.getTargetShift(),
        dto.getHardMin(),
        dto.getSoftMin(),
        dto.getMinPenalty(),
        dto.getSoftMax(),
        dto.getMaxPenalty(),
        dto.getHardMax()
    );
  }

  private final ConstraintType type = ConstraintType.EMPLOYEES_PER_SHIFT;
  private WorkShifts shift;

  public EmployeesPerShiftConstraint(
      WorkShifts shift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
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
