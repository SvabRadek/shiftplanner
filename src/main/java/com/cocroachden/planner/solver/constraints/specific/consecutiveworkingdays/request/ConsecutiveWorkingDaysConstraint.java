package com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request;


import com.cocroachden.planner.constraint.ConsecutiveWorkingDaysConstraintDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ConsecutiveWorkingDaysConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsecutiveWorkingDaysConstraint extends AbstractMinMaxConstraint implements EmployeeConstraint {
    public static ConsecutiveWorkingDaysConstraint from(ConsecutiveWorkingDaysConstraintDTO dto) {
        return new ConsecutiveWorkingDaysConstraint(
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

    public ConsecutiveWorkingDaysConstraint(
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

