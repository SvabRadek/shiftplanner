package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.constraint.RequestedShiftConstraintDTO;
import com.cocroachden.planner.constraint.ShiftsPerScheduleConstraintDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.validations.employee.ConstraintEmployeeValidator;
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
    var shiftsPerSchedule = new ShiftsPerScheduleConstraintDTO(
        UUID.randomUUID().toString(),
        employeeId.getId(),
        WorkShifts.WORKING_SHIFTS,
        3, 4, 1, 5, 1, 6
    );
    var issues = ConstraintEmployeeValidator.validate(
        new SolverConfigurationDTO(
                SolverConfigurationId.random(),
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
    Assertions.assertThat(issues.get(0).getIssue())
        .isEqualTo(
            "Pracovník nemá dostatek dní, kdy by mohl pracovat, aby splnil požadavek na minimální počet přiřazených směn.");
  }

  @Test
  public void itCanFindIssueWhenWorkerRequestsMoreThanMaximumNumberOfShiftsPerSchedule() {
    var employeeId = EmployeeId.random();
    var shiftsPerSchedule = new ShiftsPerScheduleConstraintDTO(
        UUID.randomUUID().toString(),
        employeeId.getId(),
        WorkShifts.WORKING_SHIFTS,
        0, 1, 1, 1, 1, 1
    );
    var spec1 = new RequestedShiftConstraintDTO(
        UUID.randomUUID().toString(),
        employeeId.getId(),
        LocalDate.of(1, 1, 1),
        WorkShifts.DAY
    );
    var spec2 = new RequestedShiftConstraintDTO(
        UUID.randomUUID().toString(),
        employeeId.getId(),
        LocalDate.of(1, 1, 2),
        WorkShifts.DAY
    );
    var issues = ConstraintEmployeeValidator.validate(
        new SolverConfigurationDTO(
            SolverConfigurationId.random(),
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
    Assertions.assertThat(issues.get(0).getIssue())
        .isEqualTo("Pracovník vyžaduje více směn, než je nastavený maximální limit pro počet směn na rozvrh.");
  }

  private EmployeeAssignmentDTO createEmployee(String id) {
    return new EmployeeAssignmentDTO(
            id,
        0,
        1
    );
  }

  private static String randomId() {
    return UUID.randomUUID().toString();
  }

}