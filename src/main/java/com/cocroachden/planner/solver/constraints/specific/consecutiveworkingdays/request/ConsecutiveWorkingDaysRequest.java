package com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request;


import com.cocroachden.planner.constraint.ConsecutiveWorkingDaysRequestDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ConsecutiveWorkingDaysRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsecutiveWorkingDaysRequest extends AbstractMinMaxRequest implements EmployeeConstraint {
    public static ConsecutiveWorkingDaysRequest from(ConsecutiveWorkingDaysRequestDTO dto) {
        return new ConsecutiveWorkingDaysRequest(
                EmployeeId.from(dto.getOwner()),
                dto.getTargetShift(),
                dto.getHardMin(),
                dto.getSoftMin(),
                dto.getMinPenalty(),
                dto.getSoftMax(),
                dto.getMaxPenalty(),
                dto.getHardMax()
        );
    }
    private final ConstraintType type = ConstraintType.CONSECUTIVE_WORKING_DAYS;
    private EmployeeId owner;
    private WorkShifts targetShift;

    public ConsecutiveWorkingDaysRequest(
            EmployeeId owner,
            WorkShifts targetShift,
            Integer hardMin,
            Integer softMin,
            Integer minPenalty,
            Integer softMax,
            Integer maxPenalty,
            Integer hardMax
    ) {
        super(hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
        this.owner = owner;
        this.targetShift = targetShift;
    }

    @Override
    public String toString() {
        return "{ " +
                "type: " + this.getType().toString() +
                ", targetShift: " + targetShift.getSymbol() +
                ", softMin: " + this.getSoftMin() +
                ", minPenalty: " + this.getMinPenalty() +
                ", hardMin: " + this.getHardMin() +
                ", softMax: " + this.getSoftMax() +
                ", maxPenalty: " + this.getMaxPenalty() +
                ", hardMax: " + this.getHardMax() +
                ", owner: " + this.getOwner() +
                " }";
    }
}

