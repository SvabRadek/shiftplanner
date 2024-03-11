package com.cocroachden.planner.solver.constraints.specific.workershiftrequest.validation;

import com.cocroachden.planner.constraint.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.ShiftsPerScheduleRequestDTO;
import com.cocroachden.planner.constraint.SpecificShiftRequestDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.solver.constraints.validator.IssueSeverity;
import com.cocroachden.planner.solver.constraints.validator.SpecificConstraintValidator;
import com.cocroachden.planner.solver.constraints.validator.ValidatorIssue;
import com.cocroachden.planner.solver.constraints.validator.ValidatorResult;
import com.cocroachden.planner.solver.schedule.WorkShifts;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SpecificShiftRequestValidator implements SpecificConstraintValidator {
  @Override
  public ValidatorResult validate(
      PlannerConfigurationDTO configurationRecord,
      List<ConstraintRequestDTO> constraint
  ) {
    var issues = new ArrayList<ValidatorIssue>();
    var daysInSchedule = configurationRecord.getStartDate().toDate().until(configurationRecord.getEndDate().toDate(), ChronoUnit.DAYS);
    var specificShiftRequests = constraint.stream()
        .filter(c -> c instanceof SpecificShiftRequestDTO)
        .map(c -> (SpecificShiftRequestDTO) c)
        .toList();
    var owners = specificShiftRequests.stream()
        .map(SpecificShiftRequestDTO::getOwner)
        .distinct()
        .toList();
    var shiftsPerScheduleRequests = constraint.stream()
        .filter(r -> r instanceof ShiftsPerScheduleRequestDTO)
        .map(r -> (ShiftsPerScheduleRequestDTO) r)
        .toList();

    owners.forEach(owner -> {
      var offDays = specificShiftRequests.stream()
          .filter(r -> r.getOwner().equals(owner))
          .filter(r -> r.getRequestedShift().equals(WorkShifts.OFF))
          .toList();
      int minimumShiftsToAssign = shiftsPerScheduleRequests.stream()
          .filter(r -> r.getOwner().equals(owner))
          .map(ShiftsPerScheduleRequestDTO::getHardMin)
          .reduce(Integer::sum)
          .orElse(0);
      long workableDays = daysInSchedule - offDays.size();
      if (minimumShiftsToAssign > workableDays) {
       issues.add(new ValidatorIssue(
           owner.getId(),
           IssueSeverity.ERROR,
           "Požadované směny v rozvrhu jsou v konfliktu s požadovaným počtem směn na rozvrh. Není kam přiřadit požadovaný počet směn."
       ));
      }
      if (minimumShiftsToAssign < workableDays && workableDays - minimumShiftsToAssign < 5) {
        issues.add(new ValidatorIssue(
            owner.getId(),
            IssueSeverity.WARNING,
            "Požadované směny v rozvrhu nenechávají přiliš prostoru pro řešení. Toto může ovlivnit čas pořebný k nalezení řešení."
        ));
      }
    });
    return new ValidatorResult(issues);
  }
}
