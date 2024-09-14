package com.cocroachden.planner.constraint;

import com.cocroachden.planner.solver.constraints.specific.teamassignment.request.TeamAssignmentRequest;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TeamAssignmentRequestDTO implements ConstraintRequestDTO {

  public static TeamAssignmentRequestDTO from(String id, TeamAssignmentRequest request) {
    return new TeamAssignmentRequestDTO(
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
