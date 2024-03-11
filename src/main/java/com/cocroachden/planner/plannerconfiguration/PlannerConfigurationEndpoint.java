package com.cocroachden.planner.plannerconfiguration;

import com.cocroachden.planner.constraint.*;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.plannerconfiguration.repository.ConfigurationRequestLink;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRepository;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class PlannerConfigurationEndpoint {
  private ConstraintRequestRepository constraintRequestRepository;
  private PlannerConfigurationRepository plannerConfigurationRepository;

  public @Nonnull UUID save(
      @Nonnull PlannerConfigurationDTO plannerConfigurationDTO,
      @Nonnull List<@Nonnull SpecificShiftRequestDTO> specificShiftRequestDTOS,
      @Nonnull List<@Nonnull ShiftPatternRequestDTO> shiftPatternRequestDTOS,
      @Nonnull List<@Nonnull EmployeesPerShiftRequestDTO> employeesPerShiftRequestDTOS,
      @Nonnull List<@Nonnull ShiftFollowupRestrictionRequestDTO> shiftFollowupRestrictionRequestDTOS,
      @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> shiftsPerScheduleRequestDTOS,
      @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> consecutiveWorkingDaysRequestDTOS
  ) {
    var constraintLinks = new ArrayList<ConfigurationRequestLink>();
    specificShiftRequestDTOS.stream()
        .map(dto -> new ConstraintRequestRecord(SpecificShiftRequest.from(dto)))
        .map(record -> constraintRequestRepository.save(record).getId())
        .map(id -> new ConfigurationRequestLink(ConstraintType.SPECIFIC_SHIFT_REQUEST, id))
        .forEach(constraintLinks::add);
    shiftPatternRequestDTOS.stream()
        .map(dto -> new ConstraintRequestRecord(ShiftPatternConstraintRequest.from(dto)))
        .map(record -> constraintRequestRepository.save(record).getId())
        .map(id -> new ConfigurationRequestLink(ConstraintType.SHIFT_PATTERN_CONSTRAINT, id))
        .forEach(constraintLinks::add);
    employeesPerShiftRequestDTOS.stream()
        .map(dto -> new ConstraintRequestRecord(WorkersPerShiftRequest.from(dto)))
        .map(record -> constraintRequestRepository.save(record).getId())
        .map(id -> new ConfigurationRequestLink(ConstraintType.WORKERS_PER_SHIFT, id))
        .forEach(constraintLinks::add);
    shiftFollowupRestrictionRequestDTOS.stream()
        .map(dto -> new ConstraintRequestRecord(ShiftFollowUpRestrictionRequest.from(dto)))
        .map(record -> constraintRequestRepository.save(record).getId())
        .map(id -> new ConfigurationRequestLink(ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION, id))
        .forEach(constraintLinks::add);
    shiftsPerScheduleRequestDTOS.stream()
        .map(dto -> new ConstraintRequestRecord(ShiftsPerScheduleRequest.from(dto)))
        .map(record -> constraintRequestRepository.save(record).getId())
        .map(id -> new ConfigurationRequestLink(ConstraintType.SHIFT_PER_SCHEDULE, id))
        .forEach(constraintLinks::add);
    consecutiveWorkingDaysRequestDTOS.stream()
        .map(dto -> new ConstraintRequestRecord(ConsecutiveWorkingDaysRequest.from(dto)))
        .map(record -> constraintRequestRepository.save(record).getId())
        .map(id -> new ConfigurationRequestLink(ConstraintType.CONSECUTIVE_WORKING_DAYS, id))
        .forEach(constraintLinks::add);
    return plannerConfigurationRepository.save(
        new PlannerConfigurationRecord(
            plannerConfigurationDTO.getName(),
            plannerConfigurationDTO.getStartDate().toDate(),
            plannerConfigurationDTO.getEndDate().toDate(),
            plannerConfigurationDTO.getWorkers(),
            constraintLinks
        )
    ).getId();
  }

  public @Nonnull UUID update(
      @Nonnull PlannerConfigurationDTO plannerConfigurationDTO,
      @Nonnull List<@Nonnull SpecificShiftRequestDTO> specificShiftRequestDTOS,
      @Nonnull List<@Nonnull ShiftPatternRequestDTO> shiftPatternRequestDTOS,
      @Nonnull List<@Nonnull EmployeesPerShiftRequestDTO> employeesPerShiftRequestDTOS,
      @Nonnull List<@Nonnull ShiftFollowupRestrictionRequestDTO> shiftFollowupRestrictionRequestDTOS,
      @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> shiftsPerScheduleRequestDTOS,
      @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> consecutiveWorkingDaysRequestDTOS
  ) {
    constraintRequestRepository.deleteAllById(specificShiftRequestDTOS.stream().map(SpecificShiftRequestDTO::getId).toList());
    constraintRequestRepository.deleteAllById(shiftPatternRequestDTOS.stream().map(ShiftPatternRequestDTO::getId).toList());
    constraintRequestRepository.deleteAllById(employeesPerShiftRequestDTOS.stream().map(EmployeesPerShiftRequestDTO::getId).toList());
    constraintRequestRepository.deleteAllById(shiftFollowupRestrictionRequestDTOS.stream().map(
        ShiftFollowupRestrictionRequestDTO::getId).toList());
    constraintRequestRepository.deleteAllById(shiftsPerScheduleRequestDTOS.stream().map(ShiftsPerScheduleRequestDTO::getId).toList());
    constraintRequestRepository.deleteAllById(consecutiveWorkingDaysRequestDTOS.stream().map(
        ConsecutiveWorkingDaysRequestDTO::getId).toList());
    plannerConfigurationRepository.deleteById(plannerConfigurationDTO.getId());
    return this.save(
        plannerConfigurationDTO,
        specificShiftRequestDTOS,
        shiftPatternRequestDTOS,
        employeesPerShiftRequestDTOS,
        shiftFollowupRestrictionRequestDTOS,
        shiftsPerScheduleRequestDTOS,
        consecutiveWorkingDaysRequestDTOS
    );
  }

  @Nonnull
  public List<@Nonnull PlannerConfigurationMetaDataDTO> getMetaData() {
    return StreamSupport.stream(plannerConfigurationRepository.findAll().spliterator(), false)
        .map(PlannerConfigurationMetaDataDTO::from)
        .toList();
  }

  @Nonnull
  public List<@Nonnull PlannerConfigurationDTO> findAll() {
    return StreamSupport.stream(plannerConfigurationRepository.findAll().spliterator(), false)
        .map(PlannerConfigurationDTO::from)
        .toList();
  }

  @Nonnull
  public PlannerConfigurationDTO getConfiguration(@Nonnull UUID uuid) {
    return PlannerConfigurationDTO.from(plannerConfigurationRepository.getById(uuid));
  }

  public void delete(@Nonnull UUID uuid) {
    var record = plannerConfigurationRepository.getById(uuid);
    constraintRequestRepository.deleteAllById(
        record.getConstraintRequestInstances().stream()
            .map(ConfigurationRequestLink::getRequestId)
            .toList()
    );
    plannerConfigurationRepository.deleteById(uuid);
  }

}
