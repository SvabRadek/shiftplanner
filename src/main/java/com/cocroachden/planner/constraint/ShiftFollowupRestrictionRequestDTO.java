package com.cocroachden.planner.constraint;

import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShiftFollowupRestrictionRequestDTO {
  public static ShiftFollowupRestrictionRequestDTO from(UUID id, ShiftFollowUpRestrictionRequest request) {
    return new ShiftFollowupRestrictionRequestDTO(
        id,
        ShiftFollowUpRestrictionRequest.ID,
        request.getFirstShift(),
        request.getForbiddenFollowup(),
        request.getPenalty()
    );
  }
  @Nonnull
  private UUID id;
  @Nonnull
  private ConstraintType type;
  @Nonnull
  private WorkShifts firstShift;
  @Nonnull
  private WorkShifts forbiddenFollowup;
  @Nonnull
  private Integer penalty;
}
