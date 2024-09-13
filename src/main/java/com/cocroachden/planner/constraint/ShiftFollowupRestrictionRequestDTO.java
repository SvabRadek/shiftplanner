package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.api.WorkShifts;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShiftFollowupRestrictionRequestDTO implements ConstraintRequestDTO {
  public static ShiftFollowupRestrictionRequestDTO from(String id, ShiftFollowUpRestrictionRequest request) {
    return new ShiftFollowupRestrictionRequestDTO(
        id,
        ShiftFollowUpRestrictionRequest.ID,
        request.getFirstShift(),
        request.getForbiddenFollowup(),
        request.getPenalty()
    );
  }
  @Nonnull
  private String id;
  @Nonnull
  private ConstraintType type;
  @Nonnull
  private WorkShifts firstShift;
  @Nonnull
  private WorkShifts forbiddenFollowup;
  @Nonnull
  private Integer penalty;
}
