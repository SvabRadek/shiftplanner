package com.cocroachden.planner.solver.constraints.specific.workershiftrequest;

import com.cocroachden.planner.solver.constraints.Constraint;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;

public class WorkerShiftRequestConstraint implements Constraint {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    var request = ((SpecificShiftRequest) constraintRequest);
    var workDay = schedulePlan.getSpecificDay(
        request.getOwner().orElseThrow(),
        request.getDate()
    );
    var requestedShift = request.getRequestedShift();
    var value = switch (requestedShift) {
      case NOT_DAY, NOT_NIGHT -> 0;
      default -> 1;
    };
    workDay.getShifts(requestedShift).forEach(shift -> model.addEquality(shift, value));
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof SpecificShiftRequest;
  }
}
