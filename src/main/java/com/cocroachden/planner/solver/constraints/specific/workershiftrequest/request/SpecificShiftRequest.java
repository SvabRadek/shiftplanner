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
@JsonTypeName("SpecificShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecificShiftRequest extends AbstractConstraintRequest {
  public static final ConstraintRequestTypeId ID = new ConstraintRequestTypeId("SpecificShiftRequest");
  private LocalDate date;
  private WorkShifts requestedShift;

  public SpecificShiftRequest(WorkerId owner, LocalDate date, WorkShifts requestedShift) {
    super(ID, owner);
    this.date = date;
    this.requestedShift = requestedShift;
  }
}
