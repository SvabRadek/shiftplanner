package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.constraints.specific.teamassignment.request.TeamAssignmentConstraint;
import dev.hilla.Nonnull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamAssignmentConstraintDTO implements ConstraintDTO {

  public static TeamAssignmentConstraintDTO from(String id, TeamAssignmentConstraint request) {
    return new TeamAssignmentConstraintDTO(
        id,
        request.getOwner().getId(),
        request.getTeamId(),
        request.getIsLeader(),
        request.getPenalty()
    );
  }

  @Nonnull
  private final ConstraintType type = ConstraintType.TEAM_ASSIGNMENT;
  @Nonnull
  @Setter
  private String id;
  @Nonnull
  private String owner;
  @Nonnull
  private Integer teamId;
  @Nonnull
  private Boolean isLeader;
  @Nonnull
  private Integer penalty;
}
