package com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.EmployeeShiftRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.google.ortools.sat.CpModel;

public class EmployeeShiftRequestConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
    var request = ((EmployeeShiftRequest) constraintRequest);
    var workDay = schedulePlan.getSpecificDay(
        request.getOwner(),
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
    return request instanceof EmployeeShiftRequest;
  }
}
