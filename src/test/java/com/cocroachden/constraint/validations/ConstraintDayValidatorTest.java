package com.cocroachden.constraint.validations;

import com.cocroachden.planner.constraint.RequestedShiftConstraintDTO;
import com.cocroachden.planner.constraint.EmployeesPerShiftConstraintDTO;
import com.cocroachden.planner.solverconfiguration.validations.day.ConstraintDayValidator;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
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
        var limitingRequest = new EmployeesPerShiftConstraintDTO(
                UUID.randomUUID().toString(),
                WorkShifts.WORKING_SHIFTS,
                0,
                1,
                1,
                1,
                1,
                2
        );
        var spec1 = this.createSpecificShiftRequest(new EmployeeId(UUID.randomUUID().toString()), LocalDate.of(1, 1, 1), WorkShifts.DAY);
        var spec2 = this.createSpecificShiftRequest(new EmployeeId(UUID.randomUUID().toString()), LocalDate.of(1, 1, 1), WorkShifts.NIGHT);
        var spec3 = this.createSpecificShiftRequest(new EmployeeId(UUID.randomUUID().toString()), LocalDate.of(1, 1, 1), WorkShifts.DAY);
        var issues = ConstraintDayValidator.validate(
                new SolverConfigurationDTO(
                        UUID.randomUUID().toString(),
                        "",
                        Instant.now(),
                        Instant.now(),
                        LocalDate.of(1, 1, 1),
                        LocalDate.of(1, 1, 2),
                        List.of(this.createAssignment(randomId()), this.createAssignment(randomId()), this.createAssignment(randomId())),
                        List.of(limitingRequest, spec1, spec2, spec3)
                )
        );
        Assertions.assertThat(issues).size().isEqualTo(1);
        var issueText = issues.get(0).issue();
        Assertions.assertThat(issueText).isSameAs("O směnu žádá víc lidí, než je povolený maximální limit.");
    }

    @Test
    public void itCanFindIssueWithLessWorkersAvailableForWorkThanMinimumRequiredForGivenDay() {
        var limitingRequest = new EmployeesPerShiftConstraintDTO(
                randomId(),
                WorkShifts.WORKING_SHIFTS,
                2,
                3,
                1,
                4,
                1,
                5
        );
        var spec1 = this.createSpecificShiftRequest(EmployeeId.random(), LocalDate.of(1, 1, 1), WorkShifts.OFF);
        var spec2 = this.createSpecificShiftRequest(EmployeeId.random(), LocalDate.of(1, 1, 1), WorkShifts.OFF);
        var issues = ConstraintDayValidator.validate(
                new SolverConfigurationDTO(
                        UUID.randomUUID().toString(),
                        "",
                        Instant.now(),
                        Instant.now(),
                        LocalDate.of(1, 1, 1),
                        LocalDate.of(1, 1, 2),
                        List.of(this.createAssignment(randomId()), this.createAssignment(randomId()), this.createAssignment(randomId())),
                        List.of(limitingRequest, spec1, spec2)
                )
        );
        Assertions.assertThat(issues).size().isEqualTo(1);
        var issueText = issues.get(0).issue();
        Assertions.assertThat(issueText).isSameAs(
                "Zdá se, že lidí, kterým se dá přiřadit pracovní směna, je méně než povolené minimum.");
    }

    private RequestedShiftConstraintDTO createSpecificShiftRequest(
            EmployeeId owner,
            LocalDate date,
            WorkShifts shift
    ) {
        return new RequestedShiftConstraintDTO(
                UUID.randomUUID().toString(),
                owner.getId(),
                date,
                shift
        );
    }

    private EmployeeAssignmentDTO createAssignment(String id) {
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