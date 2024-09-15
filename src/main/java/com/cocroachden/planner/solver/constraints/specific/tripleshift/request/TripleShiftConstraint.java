package com.cocroachden.planner.solver.constraints.specific.tripleshift.request;

import com.cocroachden.planner.constraint.TripleShiftConstraintDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.EmployeeSolverConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("TripleShiftConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TripleShiftConstraint implements EmployeeSolverConstraint {
    public static TripleShiftConstraint from(TripleShiftConstraintDTO requestDTO) {
        return new TripleShiftConstraint(
                EmployeeId.from(requestDTO.getOwner()),
                requestDTO.getAreAllowed(),
                requestDTO.getPenaltyForShiftTripletOutsideWeekend()
        );
    }
    private final ConstraintType type = ConstraintType.TRIPLE_SHIFTS_CONSTRAINT;
    private EmployeeId owner;
    private Boolean areAllowed;
    private Integer penaltyForShiftTripletOutsideWeekend;

    @Override
    public String toString() {
        return "{ type: %s, owner: %s, penaltyForShiftTripletOutsideWeekend: %s }"
                .formatted(
                        this.getType(),
                        this.getOwner(),
                        this.penaltyForShiftTripletOutsideWeekend
                );
    }
}
