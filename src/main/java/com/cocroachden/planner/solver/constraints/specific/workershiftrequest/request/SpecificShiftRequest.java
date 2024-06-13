package com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request;


import com.cocroachden.planner.constraint.api.SpecificShiftRequestDTO;
import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.core.identity.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@JsonTypeName("SpecificShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecificShiftRequest extends AbstractEmployeeSpecificConstraint {
  public static final ConstraintType TYPE = ConstraintType.SPECIFIC_SHIFT_REQUEST;

  public static SpecificShiftRequest from(SpecificShiftRequestDTO dto) {
    return new SpecificShiftRequest(
        dto.getOwner(),
        dto.getDate().toDate(),
        dto.getRequestedShift()
    );
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate date;
  private WorkShifts requestedShift;

  public SpecificShiftRequest(WorkerId owner, LocalDate date, WorkShifts requestedShift) {
    super(TYPE, owner);
    this.date = date;
    this.requestedShift = requestedShift;
  }

  @Override
  public String toString() {
    return "{ " +
        "type: " + this.getType() +
        ", owner: " + this.getOwner().orElse(null) +
        ", date: " + date +
        ", requestedShift: " + requestedShift +
        " }";
  }
}
