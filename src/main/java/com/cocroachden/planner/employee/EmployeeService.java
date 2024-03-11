package com.cocroachden.planner.employee;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import dev.hilla.crud.CrudRepositoryService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class EmployeeService extends CrudRepositoryService<EmployeeRecord, Long, EmployeeRepository> {
  private EmployeeRepository employeeRepository;

  @Nonnull
  public List<@Nonnull EmployeeRecord> getAllEmployees() {
    return StreamSupport
        .stream(employeeRepository.findAll().spliterator(), false)
        .toList();
  }

  @Nonnull
  public List<@Nonnull EmployeeRecord> getEmployeesExcluding(@Nonnull List<@Nonnull Long> workerIds) {
    return employeeRepository.findByIdNotIn(workerIds);
  }
}


