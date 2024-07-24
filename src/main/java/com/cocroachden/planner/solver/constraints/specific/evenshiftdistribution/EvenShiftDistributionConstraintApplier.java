package com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request.EvenShiftDistributionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;

public class EvenShiftDistributionConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
    var request = (EvenShiftDistributionRequest) constraintRequest;
    schedulePlan.getAllConstraintsOfType(ShiftsPerScheduleRequest.class).stream()
        .filter(c -> c.getOwner().orElseThrow().equals(request.getOwner().orElseThrow()))
        .findAny()
        .map(AbstractMinMaxRequest::getSoftMax)
        .ifPresent(idealShiftCount -> {
          schedulePlan.getAllScheduleDatesAsStream()
              .forEach(date -> {

              });


          var dayCount = schedulePlan.getAllScheduleDatesAsStream().count();

        });


  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof EvenShiftDistributionRequest;
  }
}
