package com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request.EvenShiftDistributionConstraint;
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
    public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
        var request = (EvenShiftDistributionConstraint) solverConstraint;
        if (request.getDistributeShiftsEvenlyThroughoutSchedule()) {
            List<LinearExpr> assignmentWeeks = new ArrayList<>();
            var workersDays = schedulePlan.getAllDaysForEmployee(request.getOwner());
            var currentWeek = LinearExpr.newBuilder();
            //Get all work assignments for each week in schedule
            for (var currentDay : workersDays) {
                currentWeek.add(currentDay.dayShiftAssignment());
                currentWeek.add(currentDay.nightShiftAssignment());
                if (currentDay.date().getDayOfWeek() == DayOfWeek.MONDAY) {
                    assignmentWeeks.add(currentWeek.build());
                    currentWeek = LinearExpr.newBuilder();
                }
            }
            var totalAssignmentCount = LinearExpr.newBuilder();
            workersDays.forEach(d -> {
                totalAssignmentCount.add(d.dayShiftAssignment());
                totalAssignmentCount.add(d.nightShiftAssignment());
            });
            var weekCountInSchedule = model.newConstant(assignmentWeeks.size());
            var targetAverageAssignmentsPerWeek = model.newIntVar(0, 7, "avg");
//            model.addDivisionEquality(targetAverageAssignmentsPerWeek, totalAssignmentCount, weekCountInSchedule);
            assignmentWeeks.forEach(weekAssignments -> {
                var specificWeekTargetAvg = model.newIntVar(0, 7, "weekAvg");
                var weekSize = model.newConstant(weekAssignments.numElements() / 2);
                var weekSizeDiff = LinearExpr.affine(weekSize, -1, 7);
                model.addMaxEquality(
                        specificWeekTargetAvg,
                        new LinearArgument[]{
                                LinearExpr.constant(0),
                                LinearExpr.newBuilder().addTerm(targetAverageAssignmentsPerWeek, 1).addTerm(weekSizeDiff, -1)
                        }
                );
                var deviationFromAvg = model.newIntVar(0, 7, "deviationFromAvg");
                model.addAbsEquality(
                        deviationFromAvg,
                        LinearExpr.newBuilder().add(specificWeekTargetAvg).addTerm(weekAssignments, -1).build()
                );
                objective.addIntCost(deviationFromAvg, request.getPenaltyForDeviationFromWeeksAverage());
            });

        }
    }

    @Override
    public boolean supports(SolverConstraint request) {
        return request instanceof EvenShiftDistributionConstraint;
    }
}
