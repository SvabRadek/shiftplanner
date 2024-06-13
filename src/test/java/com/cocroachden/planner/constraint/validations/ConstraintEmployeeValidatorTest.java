package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.constraint.api.ShiftsPerScheduleRequestDTO;
import com.cocroachden.planner.constraint.api.EmployeeShiftRequestDTO;
import com.cocroachden.planner.constraint.validations.employee.ConstraintEmployeeValidator;
import com.cocroachden.planner.core.StupidDate;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.cocroachden.planner.solver.api.WorkShifts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

class ConstraintEmployeeValidatorTest {

  @Test
  public void itCanFindIssueWhenWorkerIsNotAvailableForMinimumNumberOfShiftsPerSchedule() {
    var shiftsPerSchedule = new ShiftsPerScheduleRequestDTO(
        UUID.randomUUID(),
        new EmployeeId(0L),
        ConstraintType.SHIFT_PER_SCHEDULE,
        WorkShifts.WORKING_SHIFTS,
        3, 4, 1, 5, 1, 6
    );
    var issues = ConstraintEmployeeValidator.validate(
        new SolverConfigurationDTO(
            UUID.randomUUID(),
            "",
            Instant.now(),
            Instant.now(),
            new StupidDate(1, 1, 1),
            new StupidDate(2, 1, 1),
            List.of(new EmployeeId(0L), new EmployeeId(1L), new EmployeeId(2L)),
            List.of(shiftsPerSchedule)
        )
    );
    Assertions.assertThat(issues.size()).isEqualTo(1);
    Assertions.assertThat(issues.get(0).issue())
        .isEqualTo(
            "Pracovník nemá dostatek dní, kdy by mohl pracovat, aby splnil požadavek na minimální počet přiřazených směn.");
  }

  @Test
  public void itCanFindIssueWhenWorkerRequestsMoreThanMaximumNumberOfShiftsPerSchedule() {
    var shiftsPerSchedule = new ShiftsPerScheduleRequestDTO(
        UUID.randomUUID(),
        new EmployeeId(0L),
        ConstraintType.SHIFT_PER_SCHEDULE,
        WorkShifts.WORKING_SHIFTS,
        0, 1, 1, 1, 1, 1
    );
    var spec1 = new EmployeeShiftRequestDTO(
        UUID.randomUUID(),
        ConstraintType.EMPLOYEE_SHIFT_REQUEST,
        new EmployeeId(0L),
        new StupidDate(1, 1, 1),
        WorkShifts.DAY
    );
    var spec2 = new EmployeeShiftRequestDTO(
        UUID.randomUUID(),
        ConstraintType.EMPLOYEE_SHIFT_REQUEST,
        new EmployeeId(0L),
        new StupidDate(2, 1, 1),
        WorkShifts.DAY
    );
    var issues = ConstraintEmployeeValidator.validate(
        new SolverConfigurationDTO(
            UUID.randomUUID(),
            "",
            Instant.now(),
            Instant.now(),
            new StupidDate(1, 1, 1),
            new StupidDate(2, 1, 1),
            List.of(new EmployeeId(0L), new EmployeeId(1L), new EmployeeId(2L)),
            List.of(shiftsPerSchedule, spec1, spec2)
        )
    );
    Assertions.assertThat(issues.size())
        .isEqualTo(1);
    Assertions.assertThat(issues.get(0).issue())
        .isEqualTo("Pracovník vyžaduje více směn, než je nastavený maximální limit pro počet směn na rozvrh.");
  }
}