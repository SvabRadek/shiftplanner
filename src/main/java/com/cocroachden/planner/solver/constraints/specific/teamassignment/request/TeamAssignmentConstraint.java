package com.cocroachden.planner.solver.constraints.specific.teamassignment.request;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.constraint.TeamAssignmentConstraintDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.EmployeeSolverConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("TeamAssignmentConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamAssignmentConstraint implements EmployeeSolverConstraint {

  public static TeamAssignmentConstraint from(TeamAssignmentConstraintDTO dto) {
    return new TeamAssignmentConstraint(
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
