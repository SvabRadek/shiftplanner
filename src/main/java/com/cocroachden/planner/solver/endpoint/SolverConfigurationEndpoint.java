package com.cocroachden.planner.solver.endpoint;

import com.cocroachden.planner.solver.SolverConfigurationId;
import com.cocroachden.planner.solver.api.AssignedEmployeeDTO;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.cocroachden.planner.solver.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solver.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solver.query.SolverConfigurationQuery;
import com.cocroachden.planner.solver.repository.SolverConfigurationMetadata;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class SolverConfigurationEndpoint {
    private SolverConfigurationRepository solverConfigurationRepository;
    private final ApplicationEventPublisher publisher;
    private final SolverConfigurationQuery configurationQuery;

    @Transactional
    public @Nonnull String save(@Nonnull SolverConfigurationDTO configDTO) {
        String randomId = UUID.randomUUID().toString();
        configDTO.setId(randomId);
        publisher.publishEvent(new SaveSolverConfigurationCommand(configDTO));
        return randomId;
    }

    @Nonnull
    public List<@Nonnull SolverConfigurationMetadata> getMetaData() {
        return configurationQuery.getAllMetadata();
    }

    @Nonnull
    public List<@Nonnull SolverConfigurationDTO> findAll() {
        return StreamSupport.stream(solverConfigurationRepository.findAll().spliterator(), false)
                .map(SolverConfigurationDTO::from)
                .toList();
    }

    @Nonnull
    public SolverConfigurationDTO getConfiguration(@Nonnull String solverConfigurationId) {
        return configurationQuery.getSolverConfigurationById(new SolverConfigurationId(solverConfigurationId));
    }

    public List<AssignedEmployeeDTO> ensureAssignmentsAreOrdered(List<AssignedEmployeeDTO> assignments) {
        var index = new AtomicInteger();
        return assignments.stream()
                .sorted(Comparator.comparing(AssignedEmployeeDTO::getIndex))
                .map(a ->
                        new AssignedEmployeeDTO(
                                a.getEmployee(),
                                index.getAndIncrement(),
                                a.getWeight()
                        )
                ).toList();
    }

    public void delete(@Nonnull String solverConfigurationId) {
        publisher.publishEvent(new DeleteSolverConfigurationCommand(new SolverConfigurationId(solverConfigurationId)));
    }
}
