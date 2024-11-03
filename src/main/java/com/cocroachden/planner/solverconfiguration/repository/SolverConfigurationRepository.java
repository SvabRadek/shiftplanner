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
  List<SolverConfigurationMetadata> findByOwningUser(String username);
  Optional<SolverConfigurationRecord> findByIdAndOwningUser(String id, String username);
  Boolean existsByIdAndOwningUser(String id, String username);
}
