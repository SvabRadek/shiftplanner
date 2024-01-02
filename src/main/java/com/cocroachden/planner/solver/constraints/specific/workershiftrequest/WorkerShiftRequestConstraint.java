package com.cocroachden.planner.solver.constraints.specific.workershiftrequest;

import com.cocroachden.planner.solver.constraints.Constraint;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificNegativeShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;

public class WorkerShiftRequestConstraint implements Constraint {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    if (constraintRequest instanceof SpecificNegativeShiftRequest request) {
      var workDay = schedulePlan.getSpecificDay(
          request.getOwner().orElseThrow(),
          request.getDate()
      );
      var disabledShift = request.getDisabledShift();
      model.addEquality(workDay.getShift(disabledShift), 0);
    } else if (constraintRequest instanceof SpecificShiftRequest request) {
      var workDay = schedulePlan.getSpecificDay(
          request.getOwner().orElseThrow(),
          request.getDate()
      );
      var requestedShift = request.getRequestedShift();
      model.addEquality(workDay.getShift(requestedShift), 1);
    }
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof SpecificShiftRequest || request instanceof SpecificNegativeShiftRequest;
  }
}
