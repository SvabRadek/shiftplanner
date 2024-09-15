package com.cocroachden.planner.solverconfiguration.validations.employee;

import com.cocroachden.planner.constraint.ConsecutiveWorkingDaysConstraintDTO;
import com.cocroachden.planner.constraint.ConstraintDTO;
import com.cocroachden.planner.constraint.RequestedShiftConstraintDTO;
import com.cocroachden.planner.constraint.ShiftsPerScheduleConstraintDTO;
import com.cocroachden.planner.solverconfiguration.validations.IssueSeverity;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solver.WorkShifts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConstraintEmployeeValidator {
  public static List<EmployeeValidationIssueDTO> validate(SolverConfigurationDTO solverConfig) {
    var issues = new ArrayList<EmployeeValidationIssueDTO>();
    var constraints = solverConfig.getConstraints();
    issues.addAll(
        solverConfig.getConstraints().stream()
            .filter(c -> c instanceof ShiftsPerScheduleConstraintDTO)
            .map(c -> (ShiftsPerScheduleConstraintDTO) c)
            .map(r -> validateShiftsPerSchedule(r, solverConfig))
            .flatMap(Collection::stream)
            .toList()
    );
    issues.addAll(validateConsecutiveWorkingDays(solverConfig, constraints));
    return issues;
  }

  private static List<EmployeeValidationIssueDTO> validateShiftsPerSchedule(
      ShiftsPerScheduleConstraintDTO shiftPerSchedule,
      SolverConfigurationDTO configuration
  ) {
    var issues = new ArrayList<EmployeeValidationIssueDTO>();
    var daysInSchedule = configuration.getStartDate().datesUntil(configuration.getEndDate()).count();
    var constraints = configuration.getConstraints();
    var employeeSpecificRequests = constraints.stream()
        .filter(r -> r instanceof RequestedShiftConstraintDTO)
        .map(r -> (RequestedShiftConstraintDTO) r)
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
      issues.add(new EmployeeValidationIssueDTO(
          shiftPerSchedule.getOwner(),
          IssueSeverity.ERROR,
          "Pracovník vyžaduje více směn, než je nastavený maximální limit pro počet směn na rozvrh."
      ));
    }
    if (optimisticCountOfAvailableDaysForWorkAssignments < shiftPerSchedule.getHardMin()) {
      issues.add(new EmployeeValidationIssueDTO(
          shiftPerSchedule.getOwner(),
          IssueSeverity.ERROR,
          "Pracovník nemá dostatek dní, kdy by mohl pracovat, aby splnil požadavek na minimální počet přiřazených směn."
      ));
    }
    return issues;
  }

  private static List<EmployeeValidationIssueDTO> validateConsecutiveWorkingDays(
      SolverConfigurationDTO configuration,
      List<ConstraintDTO> constraints
  ) {
    var consecutiveWorkingDaysRequests = constraints.stream()
        .filter(c -> c instanceof ConsecutiveWorkingDaysConstraintDTO)
        .map(c -> (ConsecutiveWorkingDaysConstraintDTO) c)
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
                    .filter(c1 -> c1 instanceof RequestedShiftConstraintDTO)
                    .map(c1 -> (RequestedShiftConstraintDTO) c1)
                    .filter(c1 -> c1.getOwner().equals(assignment.getEmployeeId()))
                    .filter(c1 -> c1.getRequestedShift().isSameAs(WorkShifts.WORKING_SHIFTS))
                    .map(RequestedShiftConstraintDTO::getDate)
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
                  return new EmployeeValidationIssueDTO(
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
