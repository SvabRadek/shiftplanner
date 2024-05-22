package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.api.ShiftsPerScheduleRequestDTO;
import com.cocroachden.planner.constraint.api.SpecificShiftRequestDTO;
import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.StupidDate;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.plannerconfiguration.ConfigurationRequestLinkDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

class ConstraintWorkerValidatorTest {

  @Test
  public void itCanFindIssueWhenWorkerIsNotAvailableForMinimumNumberOfShiftsPerSchedule() {
    var shiftsPerSchedule = new ShiftsPerScheduleRequestDTO(
        UUID.randomUUID(),
        new WorkerId(0L),
        ConstraintType.SHIFT_PER_SCHEDULE,
        WorkShifts.WORKING_SHIFTS,
        3, 4, 1, 5, 1, 6
    );
    var issues = ConstraintWorkerValidator.validate(
        new PlannerConfigurationDTO(
            UUID.randomUUID(),
            "",
            Instant.now(),
            Instant.now(),
            new StupidDate(1, 1, 1),
            new StupidDate(2, 1, 1),
            List.of(new WorkerId(0L), new WorkerId(1L), new WorkerId(2L)),
            this.convertToLinks(shiftsPerSchedule)
        ),
        List.of(shiftsPerSchedule)
    );
    Assertions.assertThat(issues.size()).isEqualTo(1);
    Assertions.assertThat(issues.get(0).issue())
        .isEqualTo("Pracovník nemá dostatek dní, kdy by mohl pracovat, aby splnil požadavek na minimální počet přiřazených směn.");
  }

  @Test
  public void itCanFindIssueWhenWorkerRequestsMoreThanMaximumNumberOfShiftsPerSchedule() {
    var shiftsPerSchedule = new ShiftsPerScheduleRequestDTO(
        UUID.randomUUID(),
        new WorkerId(0L),
        ConstraintType.SHIFT_PER_SCHEDULE,
        WorkShifts.WORKING_SHIFTS,
        0, 1, 1, 1, 1, 1
    );
    var spec1 = new SpecificShiftRequestDTO(
        UUID.randomUUID(),
        ConstraintType.SPECIFIC_SHIFT_REQUEST,
        new WorkerId(0L),
        new StupidDate(1, 1, 1),
        WorkShifts.DAY
    );
    var spec2 = new SpecificShiftRequestDTO(
        UUID.randomUUID(),
        ConstraintType.SPECIFIC_SHIFT_REQUEST,
        new WorkerId(0L),
        new StupidDate(2, 1, 1),
        WorkShifts.DAY
    );
    var issues = ConstraintWorkerValidator.validate(
        new PlannerConfigurationDTO(
            UUID.randomUUID(),
            "",
            Instant.now(),
            Instant.now(),
            new StupidDate(1, 1, 1),
            new StupidDate(2, 1, 1),
            List.of(new WorkerId(0L), new WorkerId(1L), new WorkerId(2L)),
            this.convertToLinks(shiftsPerSchedule, spec1, spec2)
        ),
        List.of(shiftsPerSchedule, spec1, spec2)
    );
    Assertions.assertThat(issues.size())
        .isEqualTo(1);
    Assertions.assertThat(issues.get(0).issue())
        .isEqualTo("Pracovník vyžaduje více směn, než je nastavený maximální limit pro počet směn na rozvrh.");
  }

  private List<ConfigurationRequestLinkDTO> convertToLinks(ConstraintRequestDTO... constraints) {
    return Arrays.stream(constraints)
        .map(c -> new ConfigurationRequestLinkDTO(c.getType(), c.getId()))
        .toList();
  }

}