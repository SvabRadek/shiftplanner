package com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request;


import com.cocroachden.planner.constraint.api.ShiftsPerScheduleRequestDTO;
import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ShiftsPerScheduleRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShiftsPerScheduleRequest extends AbstractMinMaxRequest {
  public static final ConstraintType ID = ConstraintType.SHIFT_PER_SCHEDULE;

  public static ShiftsPerScheduleRequest from(ShiftsPerScheduleRequestDTO dto) {
    return new ShiftsPerScheduleRequest(
        dto.getOwner(),
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

  public ShiftsPerScheduleRequest(
      WorkerId owner,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    this(owner, WorkShifts.WORKING_SHIFTS, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
  }

  public ShiftsPerScheduleRequest(
      WorkerId owner,
      WorkShifts targetShift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(ID, owner, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
    this.targetShift = targetShift;
  }

  @Override
  public String toString() {
    return "{ type: %s, owner: %s, targetShift: %s, hardMin: %s, softMin: %s, minPenalty: %s, softMax: %s, maxPenalty: %s, hardMax: %s }"
        .formatted(
            this.getType(),
            this.getOwner().isPresent() ? getOwner().get().getId() : "null",
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
