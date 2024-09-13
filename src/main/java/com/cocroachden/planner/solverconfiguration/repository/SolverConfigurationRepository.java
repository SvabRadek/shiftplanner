package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationMetadata;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SolverConfigurationRepository extends CrudRepository<SolverConfigurationRecord, SolverConfigurationId>, JpaSpecificationExecutor<SolverConfigurationRecord> {
  SolverConfigurationRecord getById(SolverConfigurationId id);
  List<SolverConfigurationMetadata> findBy();

}
