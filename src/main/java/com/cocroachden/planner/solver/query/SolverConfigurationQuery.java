package com.cocroachden.planner.solver.query;

import com.cocroachden.planner.solver.SolverConfigurationId;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.cocroachden.planner.solver.repository.SolverConfigurationMetadata;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SolverConfigurationQuery {
    private final SolverConfigurationRepository repository;

    public Boolean exists(SolverConfigurationId configurationId) {
        return repository.existsById(configurationId);
    }

    public SolverConfigurationDTO getSolverConfigurationById(SolverConfigurationId configurationId) {
        return SolverConfigurationDTO.from(repository.findById(configurationId)
                .orElseThrow(() -> new IllegalArgumentException("Solver configuration with id [" + configurationId + "] not found!"))
        );
    }

    public List<SolverConfigurationMetadata> getAllMetadata() {
        return repository.findAllMetadata().stream().toList();
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
