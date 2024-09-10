package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.employee.endpoint.EmployeeDTO;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.api.AssignedEmployeeDTO;
import com.cocroachden.planner.solver.api.ConstraintMapper;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.cocroachden.planner.solver.repository.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class SolverConfigurationEndpoint {
  private final ConstraintRequestRepository constraintRequestRepository;
  private SolverConfigurationRepository solverConfigurationRepository;
  private EmployeeAssignmentRepository employeeAssignmentRepository;
  private EmployeeRepository employeeRepository;

  @Transactional
  public @Nonnull UUID save(@Nonnull SolverConfigurationDTO configDTO) {
    if (solverConfigurationRepository.existsById(configDTO.getId())) {
      solverConfigurationRepository.deleteById(configDTO.getId());
    }
    var persistedConfigRecord = solverConfigurationRepository.save(new SolverConfigurationRecord(UUID.randomUUID()));
    var assignedEmployeeIds = configDTO.getEmployees().stream()
        .map(AssignedEmployeeDTO::getEmployee)
        .map(EmployeeDTO::getId)
        .collect(Collectors.toSet());
    var validatedConstraints = configDTO.getConstraints().stream()
        .map(ConstraintMapper::fromDto)
        .filter(c -> {
          if (c instanceof AbstractEmployeeSpecificConstraint constraint) {
            return constraint.getOwner()
                .map(EmployeeId::getId)
                .map(assignedEmployeeIds::contains)
                .orElse(true);
          }
          return true;
        }).map(c -> {
          var record = new ConstraintRequestRecord();
          record.setType(c.getType());
          record.setRequest(c);
          record.setParent(persistedConfigRecord);
          return constraintRequestRepository.save(record);
        }).collect(Collectors.toCollection(ArrayList::new));
    var validatedAssignments = this.ensureAssignmentsAreOrdered(configDTO.getEmployees()).stream()
        .map(assignmentDTO -> {
          var assignment = new EmployeeAssignment();
          var employee = employeeRepository
              .findById(assignmentDTO.getEmployee().getId())
              .orElseThrow(() -> new IllegalArgumentException(
                  "Employee '%s' not found".formatted(assignmentDTO.getEmployee().getId())
              ));
          assignment.setEmployee(employee);
          assignment.setConfiguration(persistedConfigRecord);
          assignment.setIndex(assignmentDTO.getIndex());
          assignment.setWeight(assignmentDTO.getWeight());
          return employeeAssignmentRepository.save(assignment);
        }).toList();
    persistedConfigRecord.setName(configDTO.getName());
    persistedConfigRecord.setEndDate(configDTO.getEndDate());
    persistedConfigRecord.setStartDate(configDTO.getStartDate());
    persistedConfigRecord.setConstraintRequestRecords(validatedConstraints);
    persistedConfigRecord.setEmployeeAssignments(validatedAssignments);
    return solverConfigurationRepository.save(persistedConfigRecord).getId();
  }

  @Nonnull
  public List<@Nonnull SolverConfigurationMetadata> getMetaData() {
    return solverConfigurationRepository.findBy();
  }

  @Nonnull
  public List<@Nonnull SolverConfigurationDTO> findAll() {
    return StreamSupport.stream(solverConfigurationRepository.findAll().spliterator(), false)
        .map(SolverConfigurationDTO::from)
        .toList();
  }

  @Nonnull
  public SolverConfigurationDTO getConfiguration(@Nonnull UUID uuid) {
    return SolverConfigurationDTO.from(solverConfigurationRepository.getById(uuid));
  }

  public List<AssignedEmployeeDTO> ensureAssignmentsAreOrdered(List<AssignedEmployeeDTO> assignments) {
    var index = new AtomicInteger();
    return assignments.stream()
        .sorted(Comparator.comparing(AssignedEmployeeDTO::getIndex))
        .map(a ->
          new AssignedEmployeeDTO(
              a.getEmployee(),
              index.getAndIncrement(),
              a.getWeight()
          )
        ).toList();
  }

  public void delete(@Nonnull UUID uuid) {
    solverConfigurationRepository.deleteById(uuid);
  }
}
