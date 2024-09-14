package com.cocroachden.planner.solver.constraints.specific.tripleshift.request;

import com.cocroachden.planner.constraint.TripleShiftConstraintRequestDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

@Getter
@JsonTypeName("TripleShiftConstraintRequest")
public class TripleShiftConstraintRequest extends AbstractEmployeeSpecificConstraint {
    public static ConstraintType TYPE = ConstraintType.TRIPLE_SHIFTS_CONSTRAINT;
    private Boolean areAllowed;
    private Integer penaltyForShiftTripletOutsideWeekend;

    protected TripleShiftConstraintRequest() {
        super();
    }

    public static TripleShiftConstraintRequest from(TripleShiftConstraintRequestDTO requestDTO) {
        return new TripleShiftConstraintRequest(
                requestDTO.getOwner(),
                requestDTO.getPenaltyForShiftTripletOutsideWeekend(),
                requestDTO.getAreAllowed()
        );
    }

    public TripleShiftConstraintRequest(
            EmployeeId owner,
            Integer penaltyForShiftTripletOutsideWeekend,
            Boolean areAllowed
    ) {
        super(ConstraintType.TRIPLE_SHIFTS_CONSTRAINT, owner);
        this.penaltyForShiftTripletOutsideWeekend = penaltyForShiftTripletOutsideWeekend;
        this.areAllowed = areAllowed;
    }

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
