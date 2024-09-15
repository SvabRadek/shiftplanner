package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionConstraint;
import dev.hilla.Nonnull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShiftFollowupRestrictionConstraintDTO implements ConstraintDTO {
  public static ShiftFollowupRestrictionConstraintDTO from(String id, ShiftFollowUpRestrictionConstraint request) {
    return new ShiftFollowupRestrictionConstraintDTO(
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
  @Setter
  private String id;
  @Nonnull
  private String owner;
  @Nonnull
  private WorkShifts firstShift;
  @Nonnull
  private WorkShifts forbiddenFollowup;
  @Nonnull
  private Integer penalty;
}
