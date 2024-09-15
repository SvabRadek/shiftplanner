package com.cocroachden.planner.solverconfiguration.validations.day;

import com.cocroachden.planner.constraint.RequestedShiftConstraintDTO;
import com.cocroachden.planner.constraint.EmployeesPerShiftConstraintDTO;
import com.cocroachden.planner.solverconfiguration.validations.IssueSeverity;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solver.api.WorkShifts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstraintDayValidator {
  public static List<DayValidationIssueDTO> validate(SolverConfigurationDTO configurationRecord) {
    var issues = new ArrayList<DayValidationIssueDTO>();
    configurationRecord.getConstraints().stream()
        .filter(c -> c instanceof EmployeesPerShiftConstraintDTO)
        .map(c -> (EmployeesPerShiftConstraintDTO) c)
        .map(r -> validateEmployeesPerShiftLimit(r, configurationRecord))
        .forEach(issues::addAll);
    return issues;
  }

  private static List<DayValidationIssueDTO> validateEmployeesPerShiftLimit(
      EmployeesPerShiftConstraintDTO perShiftRequestDTO,
      SolverConfigurationDTO configurationRecord
  ) {
    var constraints = configurationRecord.getConstraints();
    var startDate = configurationRecord.getStartDate();
    var endDate = configurationRecord.getEndDate();
    return startDate.datesUntil(endDate)
        .map(day -> {
          var shiftRequestsForGivenDay = constraints.stream()
              .filter(c -> c instanceof RequestedShiftConstraintDTO)
              .map(c -> (RequestedShiftConstraintDTO) c)
              .filter(c -> c.getDate().equals(day))
              .toList();
          return evaluateGivenDay(configurationRecord, perShiftRequestDTO, day, shiftRequestsForGivenDay);
        }).flatMap(Collection::stream)
        .toList();
  }

  private static List<DayValidationIssueDTO> evaluateGivenDay(
      SolverConfigurationDTO configurationDTO,
      EmployeesPerShiftConstraintDTO employeesPerShiftRequestDTO,
      LocalDate day,
      List<RequestedShiftConstraintDTO> requests
  ) {
    var issues = new ArrayList<DayValidationIssueDTO>();
    var peopleInSchedule = configurationDTO.getEmployees().size();
    var peopleRequestingTimeOffForGivenDay = requests.stream()
        .filter(r -> r.getRequestedShift().equals(WorkShifts.OFF))
        .toList()
        .size();
    var optimisticCountOfAssignablePeople = peopleInSchedule - peopleRequestingTimeOffForGivenDay;
    var peopleRequestingGivenShiftThatDay = requests.stream()
        .filter(r -> r.getRequestedShift().isSameAs(employeesPerShiftRequestDTO.getTargetShift()))
        .toList()
        .size();
    if (peopleRequestingGivenShiftThatDay > employeesPerShiftRequestDTO.getHardMax()) {
      issues.add(new DayValidationIssueDTO(
          day,
          IssueSeverity.ERROR,
          "O směnu žádá víc lidí, než je povolený maximální limit."
      ));
    }
    if (optimisticCountOfAssignablePeople < employeesPerShiftRequestDTO.getHardMin()) {
      issues.add(new DayValidationIssueDTO(
          day,
          IssueSeverity.WARNING,
          "Zdá se, že lidí, kterým se dá přiřadit pracovní směna, je méně než povolené minimum."
      ));

    }
    return issues;
  }

}
