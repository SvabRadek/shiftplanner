package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.EmployeeShiftRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeShiftRequestDTO implements ConstraintRequestDTO {
    public static EmployeeShiftRequestDTO from(String id, EmployeeShiftRequest request) {
        return new EmployeeShiftRequestDTO(
                id,
                request.getOwner().getId(),
                request.getDate(),
                request.getRequestedShift()
        );
    }

    @Nonnull
    private final ConstraintType type = ConstraintType.EMPLOYEE_SHIFT_REQUEST;
    @Nonnull
    private String id;
    @Nonnull
    private String owner;
    @Nonnull
    private LocalDate date;
    @Nonnull
    private WorkShifts requestedShift;
}
