package com.cocroachden.planner.solver.constraints.specific.teamassignment.request;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.TeamAssignmentRequestDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("TeamAssignmentRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamAssignmentRequest extends AbstractEmployeeSpecificConstraint {

  public static TeamAssignmentRequest from(TeamAssignmentRequestDTO dto) {
    return new TeamAssignmentRequest(
        dto.getOwner(),
        dto.getTeamId(),
        dto.getIsLeader(),
        dto.getPenalty()
    );
  }

  public static ConstraintType TYPE_ID = ConstraintType.TEAM_ASSIGNMENT;
  private Integer teamId;
  private Boolean isLeader;
  private Integer penalty;

  public TeamAssignmentRequest(
      EmployeeId owner,
      Integer teamId,
      Boolean isLeader,
      Integer penalty
  ) {
    super(TYPE_ID, owner);
    this.teamId = teamId;
    this.isLeader = isLeader;
    this.penalty = penalty;
  }

  @Override
  public ConstraintType getType() {
    return TeamAssignmentRequest.TYPE_ID;
  }
}
