package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.constraint.api.ConsecutiveWorkingDaysRequestDTO;
import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.api.ShiftsPerScheduleRequestDTO;
import com.cocroachden.planner.constraint.api.SpecificShiftRequestDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.solver.schedule.WorkShifts;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConstraintWorkerValidator {
  public static List<WorkerValidationIssue> validate(
      PlannerConfigurationDTO configurationRecord,
      List<ConstraintRequestDTO> constraints
  ) {
    var issues = new ArrayList<WorkerValidationIssue>();
    issues.addAll(constraints.stream()
        .filter(c -> c instanceof ShiftsPerScheduleRequestDTO)
        .map(c -> (ShiftsPerScheduleRequestDTO) c)
        .map(r -> validateShiftsPerSchedule(r, configurationRecord, constraints))
        .flatMap(Collection::stream)
        .toList());
    issues.addAll(validateConsecutiveWorkingDays(configurationRecord, constraints));
    return issues;
  }

  private static List<WorkerValidationIssue> validateShiftsPerSchedule(
      ShiftsPerScheduleRequestDTO shiftPerSchedule,
      PlannerConfigurationDTO configuration,
      List<ConstraintRequestDTO> constraints
  ) {
    var issues = new ArrayList<WorkerValidationIssue>();
    var daysInSchedule = configuration.getStartDate().toDate().datesUntil(configuration.getEndDate().toDate()).count();
    var workersSpecificRequests = constraints.stream()
        .filter(r -> r instanceof SpecificShiftRequestDTO)
        .map(r -> (SpecificShiftRequestDTO) r)
        .filter(r -> r.getOwner().equals(shiftPerSchedule.getOwner()))
        .toList();
    var requestedDaysOff = workersSpecificRequests.stream()
        .filter(r -> r.getRequestedShift().equals(WorkShifts.OFF))
        .count();
    var requestedWorkingShifts = workersSpecificRequests.stream()
        .filter(r -> r.getRequestedShift().isSameAs(WorkShifts.WORKING_SHIFTS))
        .count();
    var optimisticCountOfAvailableDaysForWorkAssignments = daysInSchedule - requestedDaysOff;
    if (requestedWorkingShifts > shiftPerSchedule.getHardMax()) {
      issues.add(new WorkerValidationIssue(
          shiftPerSchedule.getOwner(),
          IssueSeverity.ERROR,
          "Pracovník vyžaduje více směn, než je nastavený maximální limit pro počet směn na rozvrh."
      ));
    }
    if (optimisticCountOfAvailableDaysForWorkAssignments < shiftPerSchedule.getHardMin()) {
      issues.add(new WorkerValidationIssue(
          shiftPerSchedule.getOwner(),
          IssueSeverity.ERROR,
          "Pracovník nemá dostatek dní, kdy by mohl pracovat, aby splnil požadavek na minimální počet přiřazených směn."
      ));
    }
    return issues;
  }

  private static List<WorkerValidationIssue> validateConsecutiveWorkingDays(
      PlannerConfigurationDTO configuration,
      List<ConstraintRequestDTO> constraints
  ) {
    var consecutiveWorkingDaysRequests = constraints.stream()
        .filter(c -> c instanceof ConsecutiveWorkingDaysRequestDTO)
        .map(c -> (ConsecutiveWorkingDaysRequestDTO) c)
        .toList();
    return consecutiveWorkingDaysRequests.stream()
        .filter(c -> c.getTargetShift().equals(WorkShifts.WORKING_SHIFTS))
        .findAny()
        .map(c -> {
          var maxAllowedShifts = c.getHardMax();
          var minAllowedShifts = c.getHardMin();
          return configuration.getWorkers().stream()
              .map(w -> {
                var datesOfWorkRequests = constraints.stream()
                    .filter(c1 -> c1 instanceof SpecificShiftRequestDTO)
                    .map(c1 -> (SpecificShiftRequestDTO) c1)
                    .filter(c1 -> c1.getOwner().equals(w))
                    .filter(c1 -> c1.getRequestedShift().isSameAs(WorkShifts.WORKING_SHIFTS))
                    .map(c1 -> c1.getDate().toDate())
                    .collect(Collectors.toMap(Function.identity(), Function.identity()));

                var isOverLimit = datesOfWorkRequests.values().stream()
                    .anyMatch(date -> {
                      for (int i = 1; i <= maxAllowedShifts; i++) {
                        if (!datesOfWorkRequests.containsKey(date.plusDays(i))) {
                          return false;
                        }
                      }
                      return true;
                    });
                if (isOverLimit) {
                  return new WorkerValidationIssue(
                      w,
                      IssueSeverity.ERROR,
                      "Pracovnik zada vic smen v rade, nez je povolene maximum!"
                  );
                }
                return null;
              }).filter(Objects::nonNull)
              .toList();
        }).orElseGet(ArrayList::new);
  }

}
