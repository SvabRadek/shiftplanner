package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.RequestedShiftConstraint;
import dev.hilla.Nonnull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RequestedShiftConstraintDTO implements ConstraintDTO {
    public static RequestedShiftConstraintDTO from(String id, RequestedShiftConstraint request) {
        return new RequestedShiftConstraintDTO(
                id,
                request.getOwner().getId(),
                request.getDate(),
                request.getRequestedShift()
        );
    }

    @Nonnull
    private final ConstraintType type = ConstraintType.REQUESTED_SHIFT_CONSTRAINT;
    @Nonnull
    @Setter
    private String id;
    @Nonnull
    private String owner;
    @Nonnull
    private LocalDate date;
    @Nonnull
    private WorkShifts requestedShift;
}
