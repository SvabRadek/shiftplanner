package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.api.EmployeesPerShiftRequestDTO;
import com.cocroachden.planner.constraint.api.SpecificShiftRequestDTO;
import com.cocroachden.planner.lib.StupidDate;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.solver.schedule.WorkShifts;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

public class ConstraintDayValidator {
  public static List<DayValidationIssue> validate(
      PlannerConfigurationDTO configurationRecord,
      List<ConstraintRequestDTO> constraints
  ) {
    var issues = new ArrayList<DayValidationIssue>();
    constraints.stream()
        .filter(c -> c instanceof EmployeesPerShiftRequestDTO)
        .map(c -> (EmployeesPerShiftRequestDTO) c)
        .map(r -> validateEmployeesPerShiftLimit(r, configurationRecord, constraints))
        .forEach(issues::addAll);
    return issues;
  }

  private static List<DayValidationIssue> validateEmployeesPerShiftLimit(EmployeesPerShiftRequestDTO perShiftRequestDTO, PlannerConfigurationDTO configurationRecord, List<ConstraintRequestDTO> constraints) {
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
              .filter(c -> c instanceof SpecificShiftRequestDTO)
              .map(c -> (SpecificShiftRequestDTO) c)
              .filter(c -> c.getDate().equals(StupidDate.fromDate(day)))
              .toList();
          return evaluateGivenDay(configurationRecord, perShiftRequestDTO, day, shiftRequestsForGivenDay);
        }).flatMap(Collection::stream)
        .toList();
  }

  private static List<DayValidationIssue> evaluateGivenDay(
      PlannerConfigurationDTO configurationDTO,
      EmployeesPerShiftRequestDTO employeesPerShiftRequestDTO,
      LocalDate day,
      List<SpecificShiftRequestDTO> requests
  ) {
    var issues = new ArrayList<DayValidationIssue>();
    var peopleInSchedule = configurationDTO.getWorkers().size();
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
