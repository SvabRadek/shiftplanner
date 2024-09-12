package com.cocroachden.planner.solver.repository;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.SolverConfigurationId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SolverConfigurationRepository extends CrudRepository<SolverConfigurationRecord, SolverConfigurationId>, JpaSpecificationExecutor<SolverConfigurationRecord> {
  SolverConfigurationRecord getById(SolverConfigurationId id);
  SolverConfigurationMetadata getMetadataById(SolverConfigurationId configurationId);
  List<SolverConfigurationRecord> findByEmployeeAssignments_Id_EmployeeId(EmployeeId employeeId);
  List<SolverConfigurationMetadata> findBy();

}
