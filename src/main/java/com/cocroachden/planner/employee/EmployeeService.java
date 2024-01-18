package com.cocroachden.planner.employee;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class EmployeeService {
  private EmployeeRepository employeeRepository;

  @Nonnull
  public List<@Nonnull EmployeeRecord> getAllEmployees() {
    return StreamSupport
        .stream(employeeRepository.findAll().spliterator(), false)
        .toList();
  }

  @Nonnull
  public List<@Nonnull EmployeeRecord> getEmployeesByIds(@Nonnull List<@Nonnull String> workerIds) {
    return StreamSupport
        .stream(employeeRepository.findAll().spliterator(), false)
        .filter(e -> workerIds.contains(e.getWorkerId()))
        .toList();
  }

  @Nonnull
  public List<@Nonnull EmployeeRecord> getEmployeesExcluding(@Nonnull List<@Nonnull String> workerIds) {
    return StreamSupport
        .stream(employeeRepository.findAll().spliterator(), false)
        .filter(e -> !workerIds.contains(e.getWorkerId()))
        .toList();
  }

}


