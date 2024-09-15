package com.cocroachden.planner.constraint;


import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleConstraint;
import dev.hilla.Nonnull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShiftsPerScheduleConstraintDTO implements ConstraintDTO {
    public static ShiftsPerScheduleConstraintDTO from(String id, ShiftsPerScheduleConstraint request) {
        return new ShiftsPerScheduleConstraintDTO(
                id,
                request.getOwner().getId(),
                request.getTargetShift(),
                request.getHardMin(),
                request.getSoftMin(),
                request.getMinPenalty(),
                request.getSoftMax(),
                request.getMaxPenalty(),
                request.getHardMax()
        );
    }

    @Nonnull
    private final ConstraintType type = ConstraintType.SHIFTS_PER_SCHEDULE;
    @Nonnull
    @Setter
    private String id;
    @Nonnull
    private String owner;
    @Nonnull
    private WorkShifts targetShift;
    @Nonnull
    private Integer hardMin;
    @Nonnull
    private Integer softMin;
    @Nonnull
    private Integer minPenalty;
    @Nonnull
    private Integer softMax;
    @Nonnull
    private Integer maxPenalty;
    @Nonnull
    private Integer hardMax;
}
