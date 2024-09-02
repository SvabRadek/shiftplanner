package com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request.EvenShiftDistributionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class EvenShiftDistributionConstraintApplier implements ConstraintApplier {
    @Override
    public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
        var request = (EvenShiftDistributionRequest) constraintRequest;
        if (request.getDistributeShiftsEvenlyThroughoutSchedule()) {
            List<LinearExpr> assignmentWeeks = new ArrayList<>();
            var workersDays = schedulePlan.getAllDaysForEmployee(request.getOwner().orElseThrow());
            var currentWeek = LinearExpr.newBuilder();
            for (int i = 0; i < workersDays.size(); i++) {
                var currentDay = workersDays.get(i);
                currentWeek.addTerm(currentDay.dayShiftAssignment(), 1);
                currentWeek.addTerm(currentDay.nightShiftAssignment(), 1);
                if (currentDay.date().getDayOfWeek() == DayOfWeek.MONDAY) {
                    assignmentWeeks.add(currentWeek.build());
                    currentWeek = LinearExpr.newBuilder();
                }
            }
            var totalAssignmentCount = LinearExpr.newBuilder();
            var weekCount = assignmentWeeks.size();
            workersDays.forEach(d -> {
                totalAssignmentCount.addTerm(d.dayShiftAssignment(), 1);
                totalAssignmentCount.addTerm(d.nightShiftAssignment(), 1);
            });
            var avg = model.newIntVar(0, 7, "");
            var seven = model.newConstant(7);
            var zero = model.newConstant(0);
            model.addDivisionEquality(avg, totalAssignmentCount, LinearExpr.newBuilder().add(weekCount));
            assignmentWeeks.forEach(w -> {
                var weekAvg = model.newIntVar(0, 7, "");
                var weekSize = model.newConstant(w.numElements());
                var weekSizeDiff = LinearExpr.newBuilder()
                        .addTerm(seven, 1)
                        .addTerm(weekSize, -1)
                        .build();
                model.addMaxEquality(weekAvg, new LinearArgument[]{zero, LinearExpr.newBuilder().addTerm(avg, 1).addTerm(weekSizeDiff, -1)});
                var deviationFromAvg = model.newIntVar(0, 7, "");
                model.addAbsEquality(
                        deviationFromAvg,
                        LinearExpr.newBuilder().addTerm(weekAvg, 1).addTerm(w, 1).build()
                );
                objective.addIntCost(deviationFromAvg, request.getPenaltyForDeviationFromWeeksAverage());
            });

        }
    }

    @Override
    public boolean supports(ConstraintRequest request) {
        return request instanceof EvenShiftDistributionRequest;
    }
}
