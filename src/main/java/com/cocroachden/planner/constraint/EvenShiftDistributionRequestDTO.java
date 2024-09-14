package com.cocroachden.planner.constraint;

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
                request.getOwner().getId(),
                request.getDistributeShiftsEvenlyThroughoutSchedule(),
                request.getPenaltyForDeviationFromWeeksAverage()
        );
    }

    @Nonnull
    private final ConstraintType type = ConstraintType.EVEN_SHIFT_DISTRIBUTION;
    @Nonnull
    private String id;
    @Nonnull
    private String owner;
    @Nonnull
    private Boolean distributeShiftsEvenlyThroughoutSchedule;
    @Nonnull
    private Integer penaltyForDeviationFromWeeksAverage;
}
