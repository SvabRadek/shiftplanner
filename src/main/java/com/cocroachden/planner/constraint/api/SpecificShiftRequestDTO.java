package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.core.StupidDate;
import com.cocroachden.planner.core.identity.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
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
public class SpecificShiftRequestDTO implements ConstraintRequestDTO {
  public static SpecificShiftRequestDTO from(UUID id, SpecificShiftRequest request) {
    return new SpecificShiftRequestDTO(
        id,
        SpecificShiftRequest.TYPE,
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
  private WorkerId owner;
  @Nonnull
  private StupidDate date;
  @Nonnull
  private WorkShifts requestedShift;
}
