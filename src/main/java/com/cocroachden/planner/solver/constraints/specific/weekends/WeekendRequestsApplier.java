package com.cocroachden.planner.solver.constraints.specific.weekends;

import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.weekends.request.WeekendConstraint;
import com.cocroachden.planner.solver.service.solver.SolutionObjectives;
import com.cocroachden.planner.solver.service.solver.schedule.ScheduleDay;
import com.cocroachden.planner.solver.service.solver.schedule.SchedulePlan;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearExpr;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

@Service
public class WeekendRequestsApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
    //TODO make working weekend spaced evenly
    //TODO make weekend either working or not working
    var request = (WeekendConstraint) solverConstraint;
    var owner = request.getOwner();
    var weekends = new ArrayList<ScheduleWeekend>();
    schedulePlan.getAllDaysForEmployee(owner).stream()
        .filter(day -> isWeekend(day.date().getDayOfWeek()))
        .forEach(weekendDay -> {
          weekends.stream()
              .filter(weekend -> weekend.doesBelongToThisWeekend(weekendDay.date()))
              .findAny()
              .ifPresentOrElse(
                  w -> w.setDay(weekendDay),
                  () -> {
                    var weekend = new ScheduleWeekend();
                    weekend.setDay(weekendDay);
                    weekends.add(weekend);
                  }
              );
        });
      weekends.sort(Comparator.comparing(ScheduleWeekend::getSomeDate));
    if (request.getAssignOnlyFullWorkingWeekends()) {
      weekends.forEach(w -> this.applyFullWorkingWeekendConstraint(request, w, model, objective));
    }
  }

  private void applyFullWorkingWeekendConstraint(WeekendConstraint request, ScheduleWeekend weekend, CpModel model, SolutionObjectives objective) {
    var isComplete = weekend.isComplete();
    if (isComplete) {
      var shifts = new ArrayList<BoolVar>();
      weekend.getSaturday().ifPresent(s -> shifts.addAll(s.getShifts(WorkShifts.WORKING_SHIFTS)));
      weekend.getSunday().ifPresent(s -> shifts.addAll(s.getShifts(WorkShifts.WORKING_SHIFTS)));
      var shiftsAsArray = shifts.toArray(new BoolVar[shifts.size()]);
      var zero = model.newConstant(0);
      var one = model.newConstant(1);
      var assignedShifts = model.newIntVar(0, 2, "WeekendRequestsApplier - assignedShifts");
      var isCorrect = model.newIntVar(0, 1, "WeekendRequestsApplier - isCorrect");
      model.addEquality(LinearExpr.sum(shiftsAsArray), assignedShifts);
      //expression to turn values {0, 1, 2} to {-1, 0, 1}
      var expr = LinearExpr.newBuilder().add(assignedShifts).addTerm(one, -1);
      //make variable isCorrect absolute value {0 ,1}
      model.addAbsEquality(isCorrect, expr);
      objective.addIntCost(isCorrect, request.getFullWorkWeekendPenalty() * -1);
    }
  }


  @Override
  public boolean supports(SolverConstraint request) {
    return request instanceof WeekendConstraint;
  }

  private boolean isWeekend(DayOfWeek day) {
    return day.equals(DayOfWeek.SATURDAY) || day.equals(DayOfWeek.SUNDAY);
  }

  @NoArgsConstructor
  @Setter
  private class ScheduleWeekend {
    private @Nullable ScheduleDay saturday;
    private @Nullable ScheduleDay sunday;

    public boolean isComplete() {
      return this.saturday != null && this.sunday != null;
    }

    public boolean isSameWeekend(ScheduleWeekend other) {
      if (saturday != null && other.saturday != null) {
        return saturday.date().equals(other.saturday.date());
      }
      if (sunday != null && other.sunday != null) {
        return sunday.date().equals(other.sunday.date());
      }
      if (sunday != null && other.saturday != null) {
        return sunday.date().minusDays(1).equals(other.saturday.date());
      }
      if (saturday != null && other.sunday != null) {
        return saturday.date().plusDays(1).equals(other.sunday.date());
      }
      return false;
    }

    public boolean doesBelongToThisWeekend(LocalDate date) {
      if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
        if (sunday != null) {
          return sunday.date().minusDays(1).equals(date);
        }
        if (saturday != null) {
          return saturday.date().equals(date);
        }
      }
      if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
        if (saturday != null) {
          return saturday.date().plusDays(1).equals(date);
        }
        if (sunday != null) {
          return sunday.date().equals(date);
        }
      }
      return false;
    }

    public void setDay(ScheduleDay date) {
      if (date.date().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
        this.saturday = date;
      }
      if (date.date().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
        this.sunday = date;
      }
    }

    public LocalDate getSomeDate() {
      return this.saturday != null ? this.saturday.date() : this.sunday.date();
    }

    public boolean containsDay(LocalDate date) {
      return saturday.date().equals(date) || sunday.date().equals(date);
    }

    public Optional<ScheduleDay> getSaturday() {
      return Optional.ofNullable(saturday);
    }

    public Optional<ScheduleDay> getSunday() {
      return Optional.ofNullable(sunday);
    }
  }
}
