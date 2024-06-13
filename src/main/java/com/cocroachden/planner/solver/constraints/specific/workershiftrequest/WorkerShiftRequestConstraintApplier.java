package com.cocroachden.planner.solver.constraints.specific.workershiftrequest;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.solver.Objectives;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.google.ortools.sat.CpModel;

public class WorkerShiftRequestConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    var request = ((SpecificShiftRequest) constraintRequest);
    var workDay = schedulePlan.getSpecificDay(
        request.getOwner().orElseThrow(),
        request.getDate()
    );
    var requestedShift = request.getRequestedShift();
    switch (requestedShift) {
      case NOT_DAY -> workDay.getShifts(WorkShifts.DAY).forEach(s -> model.addEquality(s, 0));
      case NOT_NIGHT -> workDay.getShifts(WorkShifts.NIGHT).forEach(s -> model.addEquality(s, 0));
      case WORKING_SHIFTS -> workDay.getShifts(WorkShifts.OFF).forEach(s -> model.addEquality(s, 0));
      case ANY -> { }
      default -> model.addEquality(workDay.getShifts(requestedShift).get(0), 1);
    }
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof SpecificShiftRequest;
  }
}
