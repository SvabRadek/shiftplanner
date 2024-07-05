package com.cocroachden.planner.solver.constraints.specific.teamassignment.request;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;

public class TeamAssignmentConstraintApplier implements ConstraintApplier {

  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
    var teamRequest = (TeamAssignmentRequest) constraintRequest;
    if (teamRequest.getOwner().isEmpty()) return;
    if (teamRequest.getIsLeader()) return;
    var employee = teamRequest.getOwner().orElseThrow();
    var teamLeader = schedulePlan.getAllConstraintsOfType(TeamAssignmentRequest.class).stream()
        .filter(r -> r.getTeamId().equals(teamRequest.getTeamId()))
        .filter(TeamAssignmentRequest::getIsLeader)
        .findFirst()
        .map(request -> request.getOwner().orElseThrow())
        .orElseThrow(() -> new IllegalArgumentException("There is no team leader in team [%s].".formatted(teamRequest.getTeamId())));
    schedulePlan.getAllScheduleDatesAsStream()
        .forEach(date -> {
          var leadersDay = schedulePlan.getSpecificDay(teamLeader, date);
          var employeesDay = schedulePlan.getSpecificDay(employee, date);
          var dayAbsInt = model.newIntVar(0, 1, "");
          var nightAbsInt = model.newIntVar(0, 1, "");
          //shift possible values from (0, 1, 2) to (-1, 0, 1)
          var isDaySame = LinearExpr.newBuilder()
              .addSum(new LinearArgument[]{ leadersDay.dayShiftAssignment(), employeesDay.dayShiftAssignment() })
              .add(-1);
          var isNightSame = LinearExpr.newBuilder()
              .addSum(new LinearArgument[]{ leadersDay.nightShiftAssignment(), employeesDay.nightShiftAssignment() })
              .add(-1);
          //convert values to absolute, so 0 or 1
          model.addMaxEquality(dayAbsInt, new LinearArgument[]{ isDaySame, isDaySame.addTerm(isDaySame, -2) });
          model.addMaxEquality(nightAbsInt, new LinearArgument[]{ isNightSame, isNightSame.addTerm(isNightSame, -2) });
          //reward if the value is 1 - so originally 0 or 2
          objective.addIntCost(dayAbsInt, teamRequest.getPenalty() * -1);
          objective.addIntCost(nightAbsInt, teamRequest.getPenalty() * -1);
        });
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof TeamAssignmentRequest;
  }
}
