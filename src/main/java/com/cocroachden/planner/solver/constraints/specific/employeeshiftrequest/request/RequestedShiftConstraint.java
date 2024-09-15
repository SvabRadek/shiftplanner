package com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request;


import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.RequestedShiftConstraintDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.EmployeeSolverConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@JsonTypeName("RequestedShiftConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestedShiftConstraint implements SolverConstraint, EmployeeSolverConstraint {
    public static RequestedShiftConstraint from(RequestedShiftConstraintDTO dto) {
        return new RequestedShiftConstraint(
                EmployeeId.from(dto.getOwner()),
                dto.getRequestedShift(), dto.getDate()
        );
    }

    private final ConstraintType type = ConstraintType.REQUESTED_SHIFT_CONSTRAINT;
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
