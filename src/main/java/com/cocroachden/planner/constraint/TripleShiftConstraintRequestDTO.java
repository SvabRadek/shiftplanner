package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TripleShiftConstraintRequestDTO implements ConstraintRequestDTO {
    public static TripleShiftConstraintRequestDTO from(String id, TripleShiftConstraintRequest request) {
        return new TripleShiftConstraintRequestDTO(
                id,
                request.getOwner().getId(),
                request.getPenaltyForShiftTripletOutsideWeekend(),
                request.getAreAllowed()
        );
    }

    @Nonnull
    private final ConstraintType type = ConstraintType.TRIPLE_SHIFTS_CONSTRAINT;
    @Nonnull
    private String id;
    @Nonnull
    private String owner;
    @Nonnull
    private Integer penaltyForShiftTripletOutsideWeekend;
    @Nonnull
    private Boolean areAllowed;
}
