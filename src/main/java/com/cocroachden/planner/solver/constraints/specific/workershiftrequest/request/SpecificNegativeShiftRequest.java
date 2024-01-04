package com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request;


import com.cocroachden.planner.lib.ConstraintRequestTypeId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@JsonTypeName("SpecificNegativeShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecificNegativeShiftRequest extends AbstractConstraintRequest {
  public static final ConstraintRequestTypeId ID = new ConstraintRequestTypeId("SpecificNegativeShiftRequest");
  private LocalDate date;
  private WorkShifts disabledShift;

  public SpecificNegativeShiftRequest(WorkerId owner, LocalDate date, WorkShifts disabledShift) {
    super(ID, owner);
    this.date = date;
    this.disabledShift = disabledShift;
  }
}
