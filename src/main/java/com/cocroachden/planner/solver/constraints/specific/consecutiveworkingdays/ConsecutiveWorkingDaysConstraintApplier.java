package com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysConstraint;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.cocroachden.planner.solver.service.schedule.ScheduleDay;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

@Service
public class ConsecutiveWorkingDaysConstraintApplier implements ConstraintApplier {
    @Override
    public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
        var request = (ConsecutiveWorkingDaysConstraint) solverConstraint;
        var owner = request.getOwner();
        var assignments = schedulePlan.getAssignments().get(owner);
        this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForEmployee(owner));
    }

    private void applyConstraint(
            CpModel model,
            SolutionObjectives objective,
            Map<LocalDate, ScheduleDay> assignments,
            ConsecutiveWorkingDaysConstraint request,
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
    public boolean supports(SolverConstraint request) {
        return request instanceof ConsecutiveWorkingDaysConstraint;
    }
}
