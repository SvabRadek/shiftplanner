package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
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
        request.getOwner().getId(),
        request.getFirstShift(),
        request.getForbiddenFollowup(),
        request.getPenalty()
    );
  }
  @Nonnull
  private final ConstraintType type = ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION;
  @Nonnull
  private String owner;
  @Nonnull
  private String id;
  @Nonnull
  private WorkShifts firstShift;
  @Nonnull
  private WorkShifts forbiddenFollowup;
  @Nonnull
  private Integer penalty;
}
