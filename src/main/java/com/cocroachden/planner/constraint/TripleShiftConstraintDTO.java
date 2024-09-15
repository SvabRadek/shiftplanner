package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraint;
import dev.hilla.Nonnull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TripleShiftConstraintDTO implements ConstraintDTO {
    public static TripleShiftConstraintDTO from(String id, TripleShiftConstraint request) {
        return new TripleShiftConstraintDTO(
                id,
                request.getOwner().getId(),
                request.getPenaltyForShiftTripletOutsideWeekend(),
                request.getAreAllowed()
        );
    }

    @Nonnull
    private final ConstraintType type = ConstraintType.TRIPLE_SHIFTS_CONSTRAINT;
    @Nonnull
    @Setter
    private String id;
    @Nonnull
    private String owner;
    @Nonnull
    private Integer penaltyForShiftTripletOutsideWeekend;
    @Nonnull
    private Boolean areAllowed;
}
