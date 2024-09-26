package com.cocroachden.planner.solver.constraints.specific.shiftpattern;


import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraint;
import com.cocroachden.planner.solver.solver.solution.SolutionObjectives;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.solver.schedule.ScheduleDay;
import com.google.ortools.sat.CpModel;
import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class ShiftPatternConstraintApplier implements ConstraintApplier {
    @Override
    public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
        var request = (ShiftPatternConstraint) solverConstraint;
        var owner = request.getOwner();
        var assignments = schedulePlan.getAssignments().get(owner);
        this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForEmployee(owner));
    }

    private void applyConstraint(
            CpModel model,
            SolutionObjectives objective,
            Map<LocalDate, ScheduleDay> assignments,
            ShiftPatternConstraint request,
            Integer weight
    ) {
        var pattern = request.getShiftPattern();
        var patternSize = request.getShiftPattern().length;
        var startIndex = request.getStartDateIndex();

        var workDays = assignments.values().stream().toList();
        for (int i = 0; i < assignments.size(); i++) {
            var adjustedIndex = (i + startIndex) % patternSize;
            var shiftForGivenDay = pattern[adjustedIndex];
            workDays.get(adjustedIndex).getShifts(shiftForGivenDay)
                    .forEach(s -> objective.addBoolCost(s, (request.getReward() * -1) * weight));
        }
    }

    @Override
    public boolean supports(SolverConstraint request) {
        return request instanceof ShiftPatternConstraint;
    }
}
