package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationMetadata;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolverConfigurationRepository extends CrudRepository<SolverConfigurationRecord, SolverConfigurationId>, JpaSpecificationExecutor<SolverConfigurationRecord> {
  SolverConfigurationRecord getById(SolverConfigurationId id);
  List<SolverConfigurationMetadata> findBy();

}
