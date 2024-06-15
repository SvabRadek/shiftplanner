package com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request;


import com.cocroachden.planner.constraint.api.EmployeeShiftRequestDTO;
import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@JsonTypeName("EmployeeShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeShiftRequest extends AbstractEmployeeSpecificConstraint {
  public static final ConstraintType TYPE = ConstraintType.EMPLOYEE_SHIFT_REQUEST;

  public static EmployeeShiftRequest from(EmployeeShiftRequestDTO dto) {
    return new EmployeeShiftRequest(
        dto.getOwner(),
        dto.getDate(),
        dto.getRequestedShift()
    );
  }

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  private LocalDate date;
  private WorkShifts requestedShift;

  public EmployeeShiftRequest(EmployeeId owner, LocalDate date, WorkShifts requestedShift) {
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
