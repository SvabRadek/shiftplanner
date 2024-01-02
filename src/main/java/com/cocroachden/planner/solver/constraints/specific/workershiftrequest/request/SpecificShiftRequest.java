package com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SpecificShiftRequest extends AbstractConstraintRequest {
  public static final ConstraintRequestId ID = new ConstraintRequestId("SpecificShiftRequest");
  private final LocalDate date;
  private final WorkShifts requestedShift;

  public SpecificShiftRequest(WorkerId owner, LocalDate date, WorkShifts requestedShift) {
    super(ID, owner);
    this.date = date;
    this.requestedShift = requestedShift;
  }
}
