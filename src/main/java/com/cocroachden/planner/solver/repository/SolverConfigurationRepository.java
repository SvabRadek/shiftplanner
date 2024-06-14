package com.cocroachden.planner.solver.repository;

import com.cocroachden.planner.employee.api.EmployeeId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SolverConfigurationRepository extends CrudRepository<SolverConfigurationRecord, UUID> {
  SolverConfigurationRecord getById(UUID id);
  List<SolverConfigurationRecord> findByEmployeesContaining(EmployeeId employeeId);
  List<SolverConfigurationMetadata> findBy();
}
