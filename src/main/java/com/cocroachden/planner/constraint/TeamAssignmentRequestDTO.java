package com.cocroachden.planner.constraint;

import com.cocroachden.planner.employee.EmployeeId;
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
        request.getOwner(),
        TeamAssignmentRequest.TYPE_ID,
        request.getTeamId(),
        request.getIsLeader(),
        request.getPenalty()
    );
  }

  @Nonnull
  private String id;
  @Nonnull
  private EmployeeId owner;
  @Nonnull
  private ConstraintType type;
  @Nonnull
  private Integer teamId;
  @Nonnull
  private Boolean isLeader;
  @Nonnull
  private Integer penalty;
}
