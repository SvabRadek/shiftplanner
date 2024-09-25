package com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction;


import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionConstraint;
import com.cocroachden.planner.solver.service.solver.SolutionObjectives;
import com.cocroachden.planner.solver.service.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.service.solver.schedule.ScheduleDay;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class ShiftFollowUpConstraintApplier implements ConstraintApplier {
    @Override
    public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
        var request = (ShiftFollowUpRestrictionConstraint) solverConstraint;
        var owner = request.getOwner();
        var assignments = schedulePlan.getAssignments().get(owner);
        this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForEmployee(owner));
    }

    private void applyConstraint(CpModel model, SolutionObjectives objective, Map<LocalDate, ScheduleDay> assignments, ShiftFollowUpRestrictionConstraint request, Integer weight) {
        assignments.forEach((date, workDay) -> {
            var shift = workDay.getShifts(request.getFirstShift()).get(0);
            var nextDay = workDay.date().plusDays(1);
            if (assignments.containsKey(nextDay)) {
                var forbiddenFollowUp = assignments.get(nextDay).getShifts(request.getForbiddenFollowup()).get(0);
                var sum = LinearExpr.newBuilder().add(shift).add(forbiddenFollowUp);
                if (request.getPenalty() == 0) {
                    model.addLessOrEqual(sum, 1);
                } else {
                    var excess = model.newIntVar(0, 1, "");
                    var zero = model.newConstant(0);
                    var expr = LinearExpr.newBuilder().add(sum).add(-1);
                    model.addMaxEquality(excess, new LinearArgument[]{expr, zero});
                    objective.addIntCost(excess, request.getPenalty() * weight);
                }
            }
        });
    }

    @Override
    public boolean supports(SolverConstraint request) {
        return request instanceof ShiftFollowUpRestrictionConstraint;
    }
}
