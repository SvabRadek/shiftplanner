package com.cocroachden.planner.solverconfiguration.validations.employee;

import com.cocroachden.planner.constraint.ConsecutiveWorkingDaysRequestDTO;
import com.cocroachden.planner.constraint.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.EmployeeShiftRequestDTO;
import com.cocroachden.planner.constraint.ShiftsPerScheduleRequestDTO;
import com.cocroachden.planner.solverconfiguration.validations.IssueSeverity;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solver.api.WorkShifts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConstraintEmployeeValidator {
  public static List<EmployeeValidationIssue> validate(SolverConfigurationDTO solverConfig) {
    var issues = new ArrayList<EmployeeValidationIssue>();
    var constraints = solverConfig.getConstraints();
    issues.addAll(
        solverConfig.getConstraints().stream()
            .filter(c -> c instanceof ShiftsPerScheduleRequestDTO)
            .map(c -> (ShiftsPerScheduleRequestDTO) c)
            .map(r -> validateShiftsPerSchedule(r, solverConfig))
            .flatMap(Collection::stream)
            .toList()
    );
    issues.addAll(validateConsecutiveWorkingDays(solverConfig, constraints));
    return issues;
  }

  private static List<EmployeeValidationIssue> validateShiftsPerSchedule(
      ShiftsPerScheduleRequestDTO shiftPerSchedule,
      SolverConfigurationDTO configuration
  ) {
    var issues = new ArrayList<EmployeeValidationIssue>();
    var daysInSchedule = configuration.getStartDate().datesUntil(configuration.getEndDate()).count();
    var constraints = configuration.getConstraints();
    var employeeSpecificRequests = constraints.stream()
        .filter(r -> r instanceof EmployeeShiftRequestDTO)
        .map(r -> (EmployeeShiftRequestDTO) r)
        .filter(r -> r.getOwner().equals(shiftPerSchedule.getOwner()))
        .toList();
    var requestedDaysOff = employeeSpecificRequests.stream()
        .filter(r -> r.getRequestedShift().equals(WorkShifts.OFF))
        .count();
    var requestedWorkingShifts = employeeSpecificRequests.stream()
        .filter(r -> r.getRequestedShift().isSameAs(WorkShifts.WORKING_SHIFTS))
        .count();
    var optimisticCountOfAvailableDaysForWorkAssignments = daysInSchedule - requestedDaysOff;
    if (requestedWorkingShifts > shiftPerSchedule.getHardMax()) {
      issues.add(new EmployeeValidationIssue(
          shiftPerSchedule.getOwner(),
          IssueSeverity.ERROR,
          "Pracovník vyžaduje více směn, než je nastavený maximální limit pro počet směn na rozvrh."
      ));
    }
    if (optimisticCountOfAvailableDaysForWorkAssignments < shiftPerSchedule.getHardMin()) {
      issues.add(new EmployeeValidationIssue(
          shiftPerSchedule.getOwner(),
          IssueSeverity.ERROR,
          "Pracovník nemá dostatek dní, kdy by mohl pracovat, aby splnil požadavek na minimální počet přiřazených směn."
      ));
    }
    return issues;
  }

  private static List<EmployeeValidationIssue> validateConsecutiveWorkingDays(
      SolverConfigurationDTO configuration,
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
          return configuration.getEmployees().stream()
              .map(assignment -> {
                var datesOfWorkRequests = constraints.stream()
                    .filter(c1 -> c1 instanceof EmployeeShiftRequestDTO)
                    .map(c1 -> (EmployeeShiftRequestDTO) c1)
                    .filter(c1 -> c1.getOwner().equals(assignment.getEmployeeId()))
                    .filter(c1 -> c1.getRequestedShift().isSameAs(WorkShifts.WORKING_SHIFTS))
                    .map(EmployeeShiftRequestDTO::getDate)
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
                  return new EmployeeValidationIssue(
                      assignment.getEmployeeId(),
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
