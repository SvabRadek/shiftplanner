package com.cocroachden.planner.solver.constraints.specific.workerspershift.request;


import com.cocroachden.planner.constraint.api.EmployeesPerShiftRequestDTO;
import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("WorkersPerShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkersPerShiftRequest extends AbstractMinMaxRequest {
  public static final ConstraintType ID = ConstraintType.WORKERS_PER_SHIFT;

  public static WorkersPerShiftRequest from(EmployeesPerShiftRequestDTO dto) {
    return new WorkersPerShiftRequest(
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
  public WorkersPerShiftRequest(
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
