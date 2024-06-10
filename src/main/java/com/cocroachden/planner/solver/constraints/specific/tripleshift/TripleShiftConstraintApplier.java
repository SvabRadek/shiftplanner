package com.cocroachden.planner.solver.constraints.specific.tripleshift;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraint;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.google.ortools.sat.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class TripleShiftConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    //detect tripleshift, middle shift has to be on weekend -> otherwise apply penalty
    var tripletRequest = (TripleShiftConstraint) constraintRequest;
    schedulePlan.assignments().forEach((workerId, days) -> {
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
              var assignedShifts = model.newIntVar(0, 3, "tripleshift constraint triplet detector");
              var two = model.newConstant(2);
              var zero = model.newConstant(0);
              var isTripletAsInt = model.newIntVar(0, 1, "");
              var expr = LinearExpr.newBuilder().add(assignedShifts).addTerm(two, -1);
              model.addEquality(LinearExpr.sum(shiftsAsArray), assignedShifts);
              model.addMaxEquality(isTripletAsInt, new LinearArgument[]{ expr, zero });
              objective.addIntCost(
                  isTripletAsInt,
                  tripletRequest.getPenaltyForShiftTripletOutsideWeekend() * schedulePlan.getWeightForWorker(workerId)
              );
            }
          });
    });
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof TripleShiftConstraint;
  }

  private boolean isWeekend(LocalDate date) {
    return date.getDayOfWeek().getValue() > 5;
  }
}
