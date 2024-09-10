package com.cocroachden.planner.employee.endpoint;

import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.cocroachden.planner.solver.repository.EmployeeAssignment;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import dev.hilla.crud.CrudRepositoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class EmployeeEndpoint extends CrudRepositoryService<EmployeeRecord, Long, EmployeeRepository> {
  private EmployeeRepository employeeRepository;
  private SolverConfigurationRepository plannerConfigurationRepository;

  @Nonnull
  public List<@Nonnull EmployeeDTO> getAllEmployees() {
    return StreamSupport
        .stream(employeeRepository.findAll().spliterator(), false)
        .map(EmployeeDTO::from)
        .toList();
  }

  @Override
  @Transactional
  public void delete(@Nonnull Long employeeId) {
    employeeRepository.findById(employeeId)
        .ifPresent(employeeRecord -> {
          plannerConfigurationRepository.findByEmployeeAssignments_Id_EmployeeId(employeeId)
              .forEach(config -> {
                var updatedEmployees = config.getEmployeeAssignments().stream()
                    .filter(id -> !id.getEmployee().equals(employeeRecord))
                    .sorted(Comparator.comparing(EmployeeAssignment::getIndex))
                    .toList();
                for (int i = 0; i < updatedEmployees.size(); i++) {
                  updatedEmployees.get(i).setIndex(i);
                }
                var updatedConstraints = config.getConstraintRequestRecords().stream()
                    .filter(r -> {
                      if (r.getRequest() instanceof AbstractEmployeeSpecificConstraint employeeSpecificConstraint) {
                        if (employeeSpecificConstraint.getOwner().isPresent()) {
                          return !employeeSpecificConstraint.getOwner().get().getId().equals(employeeRecord.getId());
                        }
                      }
                      return true;
                    }).toList();
                config.setEmployeeAssignments(updatedEmployees);
                config.setConstraintRequestRecords(updatedConstraints);
                employeeRepository.delete(employeeRecord);
              });
        });
  }

  @Nonnull
  public List<@Nonnull EmployeeRecord> getEmployeesExcluding(@Nonnull List<@Nonnull Long> employeeIds) {
    return employeeRepository.findByIdNotIn(employeeIds);
  }
}


