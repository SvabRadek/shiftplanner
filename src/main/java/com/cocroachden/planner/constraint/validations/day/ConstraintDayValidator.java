package com.cocroachden.planner.constraint.validations.day;

import com.cocroachden.planner.constraint.api.EmployeeShiftRequestDTO;
import com.cocroachden.planner.constraint.api.EmployeesPerShiftRequestDTO;
import com.cocroachden.planner.constraint.validations.IssueSeverity;
import com.cocroachden.planner.core.StupidDate;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.cocroachden.planner.solver.api.WorkShifts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class ConstraintDayValidator {
  public static List<DayValidationIssue> validate(SolverConfigurationDTO configurationRecord) {
    var issues = new ArrayList<DayValidationIssue>();
    configurationRecord.getConstraints().stream()
        .filter(c -> c instanceof EmployeesPerShiftRequestDTO)
        .map(c -> (EmployeesPerShiftRequestDTO) c)
        .map(r -> validateEmployeesPerShiftLimit(r, configurationRecord))
        .forEach(issues::addAll);
    return issues;
  }

  private static List<DayValidationIssue> validateEmployeesPerShiftLimit(
      EmployeesPerShiftRequestDTO perShiftRequestDTO,
      SolverConfigurationDTO configurationRecord
  ) {
    var constraints = configurationRecord.getConstraints();
    var startDate = configurationRecord.getStartDate().toDate();
    var endDate = configurationRecord.getEndDate().toDate();
    var dayCount = startDate.until(endDate).getDays();
    var maxEmployees = perShiftRequestDTO.getHardMax();
    var minEmployees = perShiftRequestDTO.getHardMin();
    var shift = perShiftRequestDTO.getTargetShift();
    return IntStream.range(0, dayCount)
        .mapToObj(startDate::plusDays)
        .map(day -> {
          var shiftRequestsForGivenDay = constraints.stream()
              .filter(c -> c instanceof EmployeeShiftRequestDTO)
              .map(c -> (EmployeeShiftRequestDTO) c)
              .filter(c -> c.getDate().equals(StupidDate.fromDate(day)))
              .toList();
          return evaluateGivenDay(configurationRecord, perShiftRequestDTO, day, shiftRequestsForGivenDay);
        }).flatMap(Collection::stream)
        .toList();
  }

  private static List<DayValidationIssue> evaluateGivenDay(
      SolverConfigurationDTO configurationDTO,
      EmployeesPerShiftRequestDTO employeesPerShiftRequestDTO,
      LocalDate day,
      List<EmployeeShiftRequestDTO> requests
  ) {
    var issues = new ArrayList<DayValidationIssue>();
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
      issues.add(new DayValidationIssue(
          StupidDate.fromDate(day),
          IssueSeverity.ERROR,
          "O směnu žádá víc lidí, než je povolený maximální limit."
      ));
    }
    if (optimisticCountOfAssignablePeople < employeesPerShiftRequestDTO.getHardMin()) {
      issues.add(new DayValidationIssue(
          StupidDate.fromDate(day),
          IssueSeverity.WARNING,
          "Zdá se, že lidí, kterým se dá přiřadit pracovní směna, je méně než povolené minimum."
      ));

    }
    return issues;
  }

}
