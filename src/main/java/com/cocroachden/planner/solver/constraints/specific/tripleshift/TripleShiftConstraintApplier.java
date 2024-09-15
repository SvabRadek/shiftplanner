package com.cocroachden.planner.solver.constraints.specific.tripleshift;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraint;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.cocroachden.planner.solver.service.schedule.ScheduleDay;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class TripleShiftConstraintApplier implements ConstraintApplier {
    @Override
    public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
        var tripletRequest = (TripleShiftConstraint) solverConstraint;
        var employee = tripletRequest.getOwner();
        if (!tripletRequest.getAreAllowed()) {
            var days = schedulePlan.getAllDaysForEmployee(employee);
            for (int i = 0; i < days.size() - 2; i++) {
                var day = days.get(i);
                var nextDay = days.get(i + 1);
                var dayAfter = days.get(i + 2);
                model.addLessThan(
                        LinearExpr.sum(
                                new LinearArgument[]{
                                        day.dayShiftAssignment(),
                                        day.nightShiftAssignment(),
                                        nextDay.dayShiftAssignment(),
                                        nextDay.nightShiftAssignment(),
                                        dayAfter.dayShiftAssignment(),
                                        dayAfter.nightShiftAssignment()
                                }
                        ), 3);
            }
            return;
        }
        //detect tripleshift, middle shift has to be on weekend -> otherwise apply penalty
        var assignments = schedulePlan.getAssignments().get(employee);
        var weightForEmployee = schedulePlan.getWeightForEmployee(employee);
        this.applyConstraint(schedulePlan, model, objective, weightForEmployee, assignments, tripletRequest);
    }

    private void applyConstraint(
            SchedulePlan schedulePlan,
            CpModel model,
            SolutionObjectives objective,
            Integer weight,
            Map<LocalDate, ScheduleDay> days,
            TripleShiftConstraint tripletRequest
    ) {
        schedulePlan.getStartDate().plusDays(1).datesUntil(schedulePlan.getLastDate())
                .forEach(date -> {
                    if (!isWeekend(date)) {
                        var middleWorkday = days.get(date);
                        var previousWorkday = days.get(date.minusDays(1));
                        var followingWorkday = days.get(date.plusDays(1));
                        var shifts = new ArrayList<BoolVar>();
                        shifts.addAll(middleWorkday.getShifts(WorkShifts.WORKING_SHIFTS));
                        shifts.addAll(previousWorkday.getShifts(WorkShifts.WORKING_SHIFTS));
                        shifts.addAll(followingWorkday.getShifts(WorkShifts.WORKING_SHIFTS));
                        var shiftsAsArray = shifts.toArray(BoolVar[]::new);
                        var zero = model.newConstant(0);
                        var two = model.newConstant(2);
                        var assignedShifts = model.newIntVar(0, 3, "TripleShiftConstraint - assignedShifts");
                        var isTripletAsInt = model.newIntVar(0, 1, "TripleShiftConstraint - isTripletAsInt");
                        var expr = LinearExpr.newBuilder().add(assignedShifts).addTerm(two, -1);
                        model.addEquality(LinearExpr.sum(shiftsAsArray), assignedShifts);
                        model.addMaxEquality(isTripletAsInt, new LinearArgument[]{expr, zero});
                        objective.addIntCost(
                                isTripletAsInt,
                                tripletRequest.getPenaltyForShiftTripletOutsideWeekend() * weight
                        );
                    }
                });
    }

    @Override
    public boolean supports(SolverConstraint request) {
        return request instanceof TripleShiftConstraint;
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().getValue() > 5;
    }
}
