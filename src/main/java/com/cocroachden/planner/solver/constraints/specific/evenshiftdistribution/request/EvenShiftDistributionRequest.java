package com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.EvenShiftDistributionRequestDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("EvenShiftDistributionRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvenShiftDistributionRequest extends AbstractEmployeeSpecificConstraint {
    public static ConstraintType TYPE_ID = ConstraintType.EVEN_SHIFT_DISTRIBUTION;
    private Boolean distributeShiftsEvenlyThroughoutSchedule;
    private Integer penaltyForDeviationFromWeeksAverage;

    public static EvenShiftDistributionRequest from(EvenShiftDistributionRequestDTO dto) {
        return new EvenShiftDistributionRequest(
                dto.getOwner(),
                dto.getDistributeShiftsEvenlyThroughoutSchedule(),
                dto.getPenaltyForDeviationFromWeeksAverage()
        );
    }

    public EvenShiftDistributionRequest(
            EmployeeId owner,
            Boolean distributeShiftsEvenlyThroughoutSchedule,
            Integer penaltyForDeviationFromWeeksAverage

    ) {
        super(TYPE_ID, owner);
        this.distributeShiftsEvenlyThroughoutSchedule = distributeShiftsEvenlyThroughoutSchedule;
        this.penaltyForDeviationFromWeeksAverage = penaltyForDeviationFromWeeksAverage;
    }
}
