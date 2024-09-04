package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request.EvenShiftDistributionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EvenShiftDistributionRequestDTO implements ConstraintRequestDTO {
    public static EvenShiftDistributionRequestDTO from(UUID id, EvenShiftDistributionRequest request) {
        return new EvenShiftDistributionRequestDTO(
                id,
                EvenShiftDistributionRequest.TYPE_ID,
                request.getOwner().orElse(null),
                request.getDistributeShiftsEvenlyThroughoutSchedule(),
                request.getPenaltyForDeviationFromWeeksAverage()
        );
    }

    @Nonnull
    private UUID id;
    @Nonnull
    private ConstraintType type;
    @Nonnull
    private EmployeeId owner;
    @Nonnull
    private Boolean distributeShiftsEvenlyThroughoutSchedule;
    @Nonnull
    private Integer penaltyForDeviationFromWeeksAverage;
}
