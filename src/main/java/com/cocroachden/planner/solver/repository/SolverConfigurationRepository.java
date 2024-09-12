package com.cocroachden.planner.solver.repository;

import com.cocroachden.planner.solver.SolverConfigurationId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SolverConfigurationRepository extends CrudRepository<SolverConfigurationRecord, SolverConfigurationId>, JpaSpecificationExecutor<SolverConfigurationRecord> {
  SolverConfigurationRecord getById(SolverConfigurationId id);
  List<SolverConfigurationMetadata> findBy();

}
