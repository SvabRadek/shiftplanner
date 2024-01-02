package com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SpecificNegativeShiftRequest extends AbstractConstraintRequest {
  public static final ConstraintRequestId ID = new ConstraintRequestId("SpecificNegativeShiftRequest");
  private final LocalDate date;
  private final WorkShifts disabledShift;

  public SpecificNegativeShiftRequest(WorkerId owner, LocalDate date, WorkShifts disabledShift) {
    super(ID, owner);
    this.date = date;
    this.disabledShift = disabledShift;
  }
}
