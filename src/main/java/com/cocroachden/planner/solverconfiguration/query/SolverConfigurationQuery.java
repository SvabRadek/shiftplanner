package com.cocroachden.planner.solverconfiguration.query;

import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationMetadata;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SolverConfigurationQuery {
    private final SolverConfigurationRepository repository;

    public Boolean exists(SolverConfigurationId configurationId) {
        return repository.existsById(configurationId.getId());
    }

    public SolverConfigurationDTO getSolverConfigurationById(SolverConfigurationId configurationId) {
        return SolverConfigurationDTO.from(repository.findById(configurationId.getId())
                .orElseThrow(() -> new IllegalArgumentException("Solver configuration with employeeId [" + configurationId + "] not found!"))
        );
    }

    public List<SolverConfigurationMetadata> getAllMetadata() {
        return repository.findBy().stream().toList();
    }

    public SolverConfigurationMetadata getAllMetadataById(List<SolverConfigurationId> configurationIds) {
        var stringIds = configurationIds.stream().map(SolverConfigurationId::getId).toList();
        return (SolverConfigurationMetadata) repository.findBy(
                (root, query, criteriaBuilder) -> root.get("id").in(stringIds),
                q -> q.project("id", "name", "createdAt", "lastUpdated", "startDate", "endDate")
                        .as(SolverConfigurationMetadata.class)
                        .all()
        );
    }

}
