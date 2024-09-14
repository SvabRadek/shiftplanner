package com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request;


import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.EmployeeShiftRequestDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@JsonTypeName("EmployeeShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeShiftRequest implements ConstraintRequest, EmployeeConstraint {
    public static EmployeeShiftRequest from(EmployeeShiftRequestDTO dto) {
        return new EmployeeShiftRequest(
                EmployeeId.from(dto.getOwner()),
                dto.getRequestedShift(), dto.getDate()
        );
    }

    private final ConstraintType type = ConstraintType.EMPLOYEE_SHIFT_REQUEST;
    private EmployeeId owner;
    private WorkShifts requestedShift;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    @Override
    public String toString() {
        return "{ " +
                "type: " + this.getType() +
                ", owner: " + this.getOwner() +
                ", date: " + date +
                ", requestedShift: " + requestedShift +
                " }";
    }
}
