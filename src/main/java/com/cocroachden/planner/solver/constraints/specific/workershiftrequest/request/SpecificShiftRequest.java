package com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request;


import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@JsonTypeName("SpecificShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecificShiftRequest extends AbstractConstraintRequest {
  public static final ConstraintType TYPE = ConstraintType.SPECIFIC_SHIFT_REQUEST;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate date;
  private WorkShifts requestedShift;

  public SpecificShiftRequest(WorkerId owner, LocalDate date, WorkShifts requestedShift) {
    super(TYPE, owner);
    this.date = date;
    this.requestedShift = requestedShift;
  }
}
