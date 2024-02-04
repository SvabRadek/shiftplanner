package com.cocroachden.planner.constraint;

import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.StupidDate;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SpecificShiftRequestDTO {
  public static SpecificShiftRequestDTO from(SpecificShiftRequest request) {
    return new SpecificShiftRequestDTO(
        SpecificShiftRequest.TYPE,
        request.getOwner().orElseThrow().getWorkerId(),
        StupidDate.fromDate(request.getDate()),
        request.getRequestedShift()
    );
  }
  @Nonnull
  private ConstraintType type;
  @Nonnull
  private String owner;
  @Nonnull
  private StupidDate date;
  @Nonnull
  private WorkShifts requestedShift;
}
