package com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.cocroachden.planner.solver.service.schedule.ScheduleDay;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class ConsecutiveWorkingDaysConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
    var request = (ConsecutiveWorkingDaysRequest) constraintRequest;
    var owner = request.getOwner();
    if (owner.isEmpty()) {
      schedulePlan.getAssignments().forEach((workerId, assignments) ->
          this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForWorker(workerId))
      );
    } else {
      var assignments = schedulePlan.getAssignments().get(owner.get());
      this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForWorker(owner.get()));
    }
  }

  private void applyConstraint(
      CpModel model,
      SolutionObjectives objective,
      Map<LocalDate, ScheduleDay> assignments,
      ConsecutiveWorkingDaysRequest request,
      Integer weight
  ) {
    var hardMax = request.getHardMax();
    assignments.forEach((date, workDay) -> {
      var shifts = new ArrayList<BoolVar>();
      date.datesUntil(date.plusDays(hardMax + 1)).forEach(day -> {
        if (assignments.containsKey(day)) {
          shifts.addAll(assignments.get(day).getShifts(request.getTargetShift()));
        }
      });
      MinMaxConstraint.apply(
          request,
          shifts.toArray(new BoolVar[]{}),
          request.getHardMax(),
          model,
          objective,
          weight
      );
    });
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof ConsecutiveWorkingDaysRequest;
  }
}
