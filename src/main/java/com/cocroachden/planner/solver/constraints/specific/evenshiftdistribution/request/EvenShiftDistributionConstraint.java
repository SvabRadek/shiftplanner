package com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.EvenShiftDistributionConstraintDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.EmployeeSolverConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("EvenShiftDistributionConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EvenShiftDistributionConstraint implements EmployeeSolverConstraint {
    public static EvenShiftDistributionConstraint from(EvenShiftDistributionConstraintDTO dto) {
        return new EvenShiftDistributionConstraint(
                EmployeeId.from(dto.getOwner()),
                dto.getDistributeShiftsEvenlyThroughoutSchedule(),
                dto.getPenaltyForDeviationFromWeeksAverage()
        );
    }

    private final ConstraintType type = ConstraintType.EVEN_SHIFT_DISTRIBUTION;
    private EmployeeId owner;
    private Boolean distributeShiftsEvenlyThroughoutSchedule;
    private Integer penaltyForDeviationFromWeeksAverage;
}
