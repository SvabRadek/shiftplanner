package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.constraint.api.EmployeeShiftRequestDTO;
import com.cocroachden.planner.constraint.api.ShiftsPerScheduleRequestDTO;
import com.cocroachden.planner.constraint.validations.employee.ConstraintEmployeeValidator;
import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solverconfiguration.AssignedEmployeeDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solver.api.WorkShifts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

class ConstraintEmployeeValidatorTest {

  @Test
  public void itCanFindIssueWhenWorkerIsNotAvailableForMinimumNumberOfShiftsPerSchedule() {
    var employeeId = EmployeeId.random();
    var shiftsPerSchedule = new ShiftsPerScheduleRequestDTO(
        UUID.randomUUID().toString(),
        employeeId,
        ConstraintType.SHIFT_PER_SCHEDULE,
        WorkShifts.WORKING_SHIFTS,
        3, 4, 1, 5, 1, 6
    );
    var issues = ConstraintEmployeeValidator.validate(
        new SolverConfigurationDTO(
            UUID.randomUUID().toString(),
            "",
            Instant.now(),
            Instant.now(),
            LocalDate.of(1, 1, 1),
            LocalDate.of(1, 1, 2),
            List.of(this.createEmployee(employeeId.getId()), this.createEmployee(randomId()), this.createEmployee(randomId())),
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
    var employeeId = EmployeeId.random();
    var shiftsPerSchedule = new ShiftsPerScheduleRequestDTO(
        UUID.randomUUID().toString(),
        employeeId,
        ConstraintType.SHIFT_PER_SCHEDULE,
        WorkShifts.WORKING_SHIFTS,
        0, 1, 1, 1, 1, 1
    );
    var spec1 = new EmployeeShiftRequestDTO(
        UUID.randomUUID().toString(),
        ConstraintType.EMPLOYEE_SHIFT_REQUEST,
        employeeId,
        LocalDate.of(1, 1, 1),
        WorkShifts.DAY
    );
    var spec2 = new EmployeeShiftRequestDTO(
        UUID.randomUUID().toString(),
        ConstraintType.EMPLOYEE_SHIFT_REQUEST,
        employeeId,
        LocalDate.of(1, 1, 2),
        WorkShifts.DAY
    );
    var issues = ConstraintEmployeeValidator.validate(
        new SolverConfigurationDTO(
            UUID.randomUUID().toString(),
            "",
            Instant.now(),
            Instant.now(),
            LocalDate.of(1, 1, 1),
            LocalDate.of(1, 1, 2),
            List.of(this.createEmployee(employeeId.getId()), this.createEmployee(randomId()), this.createEmployee(randomId())),
            List.of(shiftsPerSchedule, spec1, spec2)
        )
    );
    Assertions.assertThat(issues.size())
        .isEqualTo(1);
    Assertions.assertThat(issues.get(0).issue())
        .isEqualTo("Pracovník vyžaduje více směn, než je nastavený maximální limit pro počet směn na rozvrh.");
  }

  private AssignedEmployeeDTO createEmployee(String id) {
    return new AssignedEmployeeDTO(
        new EmployeeDTO(
            id,
            "irrelevant",
            "irrelevant"
        ),
        0,
        1
    );
  }

  private static String randomId() {
    return UUID.randomUUID().toString();
  }

}