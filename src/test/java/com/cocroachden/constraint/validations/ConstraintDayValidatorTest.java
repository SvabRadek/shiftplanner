package com.cocroachden.constraint.validations;

import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.constraint.api.EmployeeShiftRequestDTO;
import com.cocroachden.planner.constraint.api.EmployeesPerShiftRequestDTO;
import com.cocroachden.planner.constraint.validations.day.ConstraintDayValidator;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.cocroachden.planner.solver.api.WorkShifts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

class ConstraintDayValidatorTest {

  @Test
  public void itCanFindIssueWithMoreWorkersRequestingWorkingShiftsThanAllowed() {
    var limitingRequest = new EmployeesPerShiftRequestDTO(
        UUID.randomUUID(),
        ConstraintType.EMPLOYEES_PER_SHIFT,
        WorkShifts.WORKING_SHIFTS,
        0,
        1,
        1,
        1,
        1,
        2
    );
    var spec1 = this.createSpecificShiftRequest(new EmployeeId(0L), LocalDate.of(1, 1, 1), WorkShifts.DAY);
    var spec2 = this.createSpecificShiftRequest(new EmployeeId(1L), LocalDate.of(1, 1, 1), WorkShifts.NIGHT);
    var spec3 = this.createSpecificShiftRequest(new EmployeeId(2L), LocalDate.of(1, 1, 1), WorkShifts.DAY);
    var issues = ConstraintDayValidator.validate(
        new SolverConfigurationDTO(
            UUID.randomUUID(),
            "",
            Instant.now(),
            Instant.now(),
            LocalDate.of(1, 1, 1),
            LocalDate.of(1, 1, 2),
            List.of(new EmployeeId(0L), new EmployeeId(1L), new EmployeeId(2L)),
            List.of(limitingRequest, spec1, spec2, spec3)
        )
    );
    Assertions.assertThat(issues).size().isEqualTo(1);
    var issueText = issues.get(0).issue();
    Assertions.assertThat(issueText).isSameAs("O směnu žádá víc lidí, než je povolený maximální limit.");
  }

  @Test
  public void itCanFindIssueWithLessWorkersAvailableForWorkThanMinimumRequiredForGivenDay() {
    var limitingRequest = new EmployeesPerShiftRequestDTO(
        UUID.randomUUID(),
        ConstraintType.EMPLOYEES_PER_SHIFT,
        WorkShifts.WORKING_SHIFTS,
        2,
        3,
        1,
        4,
        1,
        5
    );
    var spec1 = this.createSpecificShiftRequest(new EmployeeId(0L), LocalDate.of(1, 1, 1), WorkShifts.OFF);
    var spec2 = this.createSpecificShiftRequest(new EmployeeId(1L), LocalDate.of(1, 1, 1), WorkShifts.OFF);
    var issues = ConstraintDayValidator.validate(
        new SolverConfigurationDTO(
            UUID.randomUUID(),
            "",
            Instant.now(),
            Instant.now(),
            LocalDate.of(1, 1, 1),
            LocalDate.of(1, 1, 2),
            List.of(new EmployeeId(0L), new EmployeeId(1L), new EmployeeId(2L)),
            List.of(limitingRequest, spec1, spec2)
        )
    );
    Assertions.assertThat(issues).size().isEqualTo(1);
    var issueText = issues.get(0).issue();
    Assertions.assertThat(issueText).isSameAs(
        "Zdá se, že lidí, kterým se dá přiřadit pracovní směna, je méně než povolené minimum.");
  }

  private EmployeeShiftRequestDTO createSpecificShiftRequest(
      EmployeeId owner,
      LocalDate date,
      WorkShifts shift
  ) {
    return new EmployeeShiftRequestDTO(
        UUID.randomUUID(),
        ConstraintType.EMPLOYEE_SHIFT_REQUEST,
        owner,
        date,
        shift
    );
  }

}