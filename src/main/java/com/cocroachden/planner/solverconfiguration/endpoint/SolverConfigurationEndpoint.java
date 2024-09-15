package com.cocroachden.planner.solverconfiguration.endpoint;

import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationMetadata;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.query.SolverConfigurationQuery;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class SolverConfigurationEndpoint {
    private final ApplicationEventPublisher publisher;
    private final SolverConfigurationQuery configurationQuery;

    @Transactional
    public @Nonnull String save(@Nonnull SolverConfigurationDTO configDTO) {
        String randomId = UUID.randomUUID().toString();
        configDTO.setId(randomId);
        configDTO.getConstraints().forEach(constraintDTO -> constraintDTO.setId(UUID.randomUUID().toString()));
        publisher.publishEvent(SaveSolverConfigurationCommand.from(configDTO));
        return randomId;
    }

    @Nonnull
    public List<@Nonnull SolverConfigurationMetadata> getMetaData() {
        return configurationQuery.getAllMetadata();
    }

    @Nonnull
    public SolverConfigurationDTO getConfiguration(@Nonnull String solverConfigurationId) {
        SolverConfigurationId typedId = new SolverConfigurationId(solverConfigurationId);
        return configurationQuery.getSolverConfigurationById(typedId);
    }

    public void delete(@Nonnull String solverConfigurationId) {
        SolverConfigurationId typedId = new SolverConfigurationId(solverConfigurationId);
        publisher.publishEvent(new DeleteSolverConfigurationCommand(typedId));
    }
}
