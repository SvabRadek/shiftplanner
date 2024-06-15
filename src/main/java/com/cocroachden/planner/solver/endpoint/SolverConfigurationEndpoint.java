package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.solver.api.ConstraintMapper;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.cocroachden.planner.solver.repository.SolverConfigurationMetadata;
import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class SolverConfigurationEndpoint {
  private SolverConfigurationRepository solverConfigurationRepository;

  public @Nonnull UUID save(@Nonnull SolverConfigurationDTO solverConfig) {
    var configurationRecord = solverConfigurationRepository
        .findById(solverConfig.getId())
        .orElse(new SolverConfigurationRecord());
    configurationRecord.setName(solverConfig.getName());
    configurationRecord.setEndDate(solverConfig.getEndDate());
    configurationRecord.setStartDate(solverConfig.getStartDate());
    configurationRecord.setEmployees(solverConfig.getEmployees());
    configurationRecord.getConstraintRequestRecords().clear();
    configurationRecord.setConstraintRequestRecords(
        solverConfig.getConstraints().stream()
            .map(r -> {
              var record = new ConstraintRequestRecord();
              record.setType(r.getType());
              record.setRequest(ConstraintMapper.fromDto(r));
              record.setParent(configurationRecord);
              return record;
            }).collect(Collectors.toCollection(ArrayList::new))
    );
    return solverConfigurationRepository.save(configurationRecord).getId();
  }

  @Nonnull
  public List<@Nonnull SolverConfigurationMetadata> getMetaData() {
    return solverConfigurationRepository.findBy();
  }

  @Nonnull
  public List<@Nonnull SolverConfigurationDTO> findAll() {
    return StreamSupport.stream(solverConfigurationRepository.findAll().spliterator(), false)
        .map(SolverConfigurationDTO::from)
        .toList();
  }

  @Nonnull
  public SolverConfigurationDTO getConfiguration(@Nonnull UUID uuid) {
    return SolverConfigurationDTO.from(solverConfigurationRepository.getById(uuid));
  }

  public void delete(@Nonnull UUID uuid) {
    solverConfigurationRepository.deleteById(uuid);
  }
}
