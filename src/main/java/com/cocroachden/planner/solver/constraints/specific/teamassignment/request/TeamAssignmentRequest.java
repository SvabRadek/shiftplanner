package com.cocroachden.planner.solver.constraints.specific.teamassignment.request;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.TeamAssignmentRequestDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("TeamAssignmentRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamAssignmentRequest implements EmployeeConstraint {

  public static TeamAssignmentRequest from(TeamAssignmentRequestDTO dto) {
    return new TeamAssignmentRequest(
            EmployeeId.from(dto.getOwner()),
            dto.getTeamId(),
            dto.getIsLeader(),
            dto.getPenalty()
    );
  }

  private final ConstraintType type = ConstraintType.TEAM_ASSIGNMENT;
  private EmployeeId owner;
  private Integer teamId;
  private Boolean isLeader;
  private Integer penalty;
}
