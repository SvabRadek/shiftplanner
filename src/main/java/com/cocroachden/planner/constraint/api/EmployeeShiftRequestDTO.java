package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.EmployeeShiftRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeShiftRequestDTO implements ConstraintRequestDTO {
  public static EmployeeShiftRequestDTO from(UUID id, EmployeeShiftRequest request) {
    return new EmployeeShiftRequestDTO(
        id,
        EmployeeShiftRequest.TYPE,
        request.getOwner().orElseThrow(),
        request.getDate(),
        request.getRequestedShift()
    );
  }

  @Nonnull
  private UUID id;
  @Nonnull
  private ConstraintType type;
  @Nonnull
  private EmployeeId owner;
  @Nonnull
  private LocalDate date;
  @Nonnull
  private WorkShifts requestedShift;
}
