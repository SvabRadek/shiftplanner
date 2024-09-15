package com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request;


import com.cocroachden.planner.constraint.ShiftsPerScheduleConstraintDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxConstraint;
import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ShiftsPerScheduleConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShiftsPerScheduleConstraint extends AbstractMinMaxConstraint implements EmployeeConstraint {
  public static ShiftsPerScheduleConstraint from(ShiftsPerScheduleConstraintDTO dto) {
    return new ShiftsPerScheduleConstraint(
        EmployeeId.from(dto.getOwner()),
        dto.getTargetShift(),
        dto.getHardMin(),
        dto.getSoftMin(),
        dto.getMinPenalty(),
        dto.getSoftMax(),
        dto.getMaxPenalty(),
        dto.getHardMax()
    );
  }

  private final ConstraintType type = ConstraintType.SHIFTS_PER_SCHEDULE;
  private EmployeeId owner;
  private WorkShifts targetShift;

  public ShiftsPerScheduleConstraint(
      EmployeeId owner,
      WorkShifts targetShift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
    this.owner = owner;
    this.targetShift = targetShift;
  }

  @Override
  public String toString() {
    return "{ type: %s, owner: %s, targetShift: %s, hardMin: %s, softMin: %s, minPenalty: %s, softMax: %s, maxPenalty: %s, hardMax: %s }"
        .formatted(
            this.getType(),
            this.getOwner().getId(),
            this.getTargetShift().getSymbol(),
            this.getHardMin(),
            this.getSoftMin(),
            this.getMinPenalty(),
            this.getSoftMax(),
            this.getMaxPenalty(),
            this.getHardMax()
        );
  }
}
