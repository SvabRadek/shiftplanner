package com.cocroachden.planner.constraint;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request.EvenShiftDistributionRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EvenShiftDistributionRequestDTO implements ConstraintRequestDTO {
    public static EvenShiftDistributionRequestDTO from(String id, EvenShiftDistributionRequest request) {
        return new EvenShiftDistributionRequestDTO(
                id,
                EvenShiftDistributionRequest.TYPE_ID,
                request.getOwner(),
                request.getDistributeShiftsEvenlyThroughoutSchedule(),
                request.getPenaltyForDeviationFromWeeksAverage()
        );
    }

    @Nonnull
    private String id;
    @Nonnull
    private ConstraintType type;
    @Nonnull
    private EmployeeId owner;
    @Nonnull
    private Boolean distributeShiftsEvenlyThroughoutSchedule;
    @Nonnull
    private Integer penaltyForDeviationFromWeeksAverage;
}
