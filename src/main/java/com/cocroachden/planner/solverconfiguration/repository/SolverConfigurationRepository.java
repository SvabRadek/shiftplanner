package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.solverconfiguration.SolverConfigurationMetadata;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolverConfigurationRepository extends CrudRepository<SolverConfigurationRecord, String>, JpaSpecificationExecutor<SolverConfigurationRecord> {
  SolverConfigurationRecord getById(String id);
  List<SolverConfigurationMetadata> findBy();
  List<SolverConfigurationMetadata> findByUsername(String username);
  Optional<SolverConfigurationRecord> findByIdAndUsername(String id, String username);


}
