package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.core.StupidDate;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.EmployeeShiftRequest;
import com.cocroachden.planner.solver.api.WorkShifts;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        StupidDate.fromDate(request.getDate()),
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
  private StupidDate date;
  @Nonnull
  private WorkShifts requestedShift;
}
