package com.cocroachden.planner.employee;

import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import dev.hilla.crud.CrudRepositoryService;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class EmployeeEndpoint extends CrudRepositoryService<EmployeeRecord, Long, EmployeeRepository> {
  private EmployeeRepository employeeRepository;
  private PlannerConfigurationRepository plannerConfigurationRepository;

  @Nonnull
  public List<@Nonnull EmployeeRecord> getAllEmployees() {
    return StreamSupport
        .stream(employeeRepository.findAll().spliterator(), false)
        .toList();
  }
  
  @Override
  public void delete(Long aLong) {
    plannerConfigurationRepository.findByWorkersContaining(new WorkerId(aLong)).stream()
        .filter(Objects::nonNull)
        .forEach(config -> {
          var newConfig = new PlannerConfigurationRecord();
          newConfig.setId(config.getId());
          newConfig.setName(config.getName());
          newConfig.setStartDate(config.getStartDate());
          newConfig.setEndDate(config.getEndDate());
          newConfig.setWorkers(
              config.getWorkers().stream()
                  .filter(workerId -> !workerId.getId().equals(aLong))
                  .toList()
          );
          newConfig.setConstraintRequestInstances(config.getConstraintRequestInstances());
          newConfig.setCreatedAt(config.getCreatedAt());
          newConfig.setLastUpdated(Instant.now());
          plannerConfigurationRepository.save(newConfig);
        });
    super.delete(aLong);
  }

  @Nonnull
  public List<@Nonnull EmployeeRecord> getEmployeesExcluding(@Nonnull List<@Nonnull Long> workerIds) {
    return employeeRepository.findByIdNotIn(workerIds);
  }
}


