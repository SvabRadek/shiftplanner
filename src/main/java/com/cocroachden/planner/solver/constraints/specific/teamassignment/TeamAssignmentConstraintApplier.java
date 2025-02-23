package com.cocroachden.planner.solver.constraints.specific.teamassignment;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.teamassignment.request.TeamAssignmentConstraint;
import com.cocroachden.planner.solver.solver.solution.SolutionObjectives;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearExpr;
import org.springframework.stereotype.Service;

@Service
public class TeamAssignmentConstraintApplier implements ConstraintApplier {

    @Override
    public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
        var teamRequest = (TeamAssignmentConstraint) solverConstraint;
        if (teamRequest.getIsLeader()) return;
        var employee = teamRequest.getOwner();
        var teamLeader = schedulePlan.getAllConstraintsOfType(TeamAssignmentConstraint.class).stream()
                .filter(r -> r.getTeamId().equals(teamRequest.getTeamId()))
                .filter(TeamAssignmentConstraint::getIsLeader)
                .findFirst()
                .map(TeamAssignmentConstraint::getOwner)
                .orElseThrow(() -> new IllegalArgumentException("There is no team leader in team [%s].".formatted(teamRequest.getTeamId())));
        schedulePlan.getAllScheduleDatesAsStream()
                .forEach(date -> {
                    var leadersDay = schedulePlan.getSpecificDay(teamLeader, date);
                    var employeesDay = schedulePlan.getSpecificDay(employee, date);

                    var areDayShiftsSame = model.newBoolVar("");
                    var areNightShiftsSame = model.newBoolVar("");
                    //shift possible values from (0, 1, 2) to (-1, 0, 1)
                    var dayShiftsCombined = LinearExpr.newBuilder()
                            .addTerm(leadersDay.dayShiftAssignment(), 1)
                            .addTerm(employeesDay.dayShiftAssignment(), 1)
                            .add(-1);
                    var nightShiftsCombined = LinearExpr.newBuilder()
                            .addTerm(leadersDay.nightShiftAssignment(), 1)
                            .addTerm(employeesDay.nightShiftAssignment(), 1)
                            .add(-1);
                    model.addAbsEquality(areDayShiftsSame, dayShiftsCombined);
                    model.addAbsEquality(areNightShiftsSame, nightShiftsCombined);
                    objective.addBoolCost(areDayShiftsSame, teamRequest.getPenalty() * -1);
                    objective.addBoolCost(areNightShiftsSame, teamRequest.getPenalty() * -1);
                });
    }

    @Override
    public boolean supports(SolverConstraint request) {
        return request instanceof TeamAssignmentConstraint;
    }
}
