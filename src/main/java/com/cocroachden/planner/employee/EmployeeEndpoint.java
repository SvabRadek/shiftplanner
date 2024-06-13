package com.cocroachden.planner.employee;

import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import dev.hilla.crud.CrudRepositoryService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class EmployeeEndpoint extends CrudRepositoryService<EmployeeRecord, Long, EmployeeRepository> {
  private EmployeeRepository employeeRepository;
  private SolverConfigurationRepository plannerConfigurationRepository;

  @Nonnull
  public List<@Nonnull EmployeeRecord> getAllEmployees() {
    return StreamSupport
        .stream(employeeRepository.findAll().spliterator(), false)
        .toList();
  }

  @Override
  public void delete(@Nonnull Long workerId) {
    var typedWorkerId = new EmployeeId(workerId);
    plannerConfigurationRepository.findByWorkersContaining(typedWorkerId).stream()
        .filter(Objects::nonNull)
        .forEach(config -> {
          var updatedWorkers = config.getWorkers().stream()
              .filter(id -> !id.equals(typedWorkerId))
              .toList();
          var updatedConstraints = config.getConstraintRequestRecords().stream()
              .filter(r -> {
                if (r.getRequest() instanceof AbstractEmployeeSpecificConstraint employeeSpecificConstraint) {
                  if (employeeSpecificConstraint.getOwner().isPresent()) {
                    return !employeeSpecificConstraint.getOwner().get().equals(typedWorkerId);
                  }
                }
                return true;
              }).toList();
          config.setWorkers(updatedWorkers);
          config.setConstraintRequestRecords(updatedConstraints);
          plannerConfigurationRepository.save(config);
        });
    super.delete(workerId);
  }

  @Nonnull
  public List<@Nonnull EmployeeRecord> getEmployeesExcluding(@Nonnull List<@Nonnull Long> workerIds) {
    return employeeRepository.findByIdNotIn(workerIds);
  }
}


