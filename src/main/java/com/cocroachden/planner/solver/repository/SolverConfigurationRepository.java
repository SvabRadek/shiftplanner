package com.cocroachden.planner.solver.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SolverConfigurationRepository extends CrudRepository<SolverConfigurationRecord, UUID> {
  SolverConfigurationRecord getById(UUID id);

  List<SolverConfigurationMetadata> findBy();

  List<SolverConfigurationRecord> findByEmployeeAssignments_Id_EmployeeId(Long employeeId);
}
