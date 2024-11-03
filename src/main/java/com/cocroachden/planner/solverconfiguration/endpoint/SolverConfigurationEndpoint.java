package com.cocroachden.planner.solverconfiguration.endpoint;

import com.cocroachden.planner.security.Role;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationMetadata;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.query.SolverConfigurationQuery;
import com.vaadin.flow.server.VaadinRequest;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.UUID;

@BrowserCallable
@AllArgsConstructor
@RolesAllowed(Role.ROLE_USER)
public class SolverConfigurationEndpoint {
    private final ApplicationEventPublisher publisher;
    private final SolverConfigurationQuery configurationQuery;

    public @Nonnull String save(@Nonnull SolverConfigurationDTO configDTO) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        if (configurationQuery.exists(configDTO.getId(), username)) {
            this.publisher.publishEvent(new DeleteSolverConfigurationCommand(configDTO.getId(), username));
        }
        var randomId = UUID.randomUUID().toString();
        configDTO.setId(randomId);
        configDTO.getConstraints().forEach(constraintDTO -> constraintDTO.setId(UUID.randomUUID().toString()));
        publisher.publishEvent(SaveSolverConfigurationCommand.from(username, configDTO));
        return randomId;
    }

    @Nonnull
    public List<@Nonnull SolverConfigurationMetadata> getMetaData() {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        return configurationQuery.getAllMetadataForUser(username);
    }

    @Nonnull
    public SolverConfigurationDTO getConfiguration(@Nonnull String solverConfigurationId) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        var typedId = SolverConfigurationId.from(solverConfigurationId);
        return configurationQuery.getSolverConfigurationById(username, typedId);
    }

    @Nonnull
    public Boolean exists(@Nonnull String solverConfigurationId) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        var typedId = SolverConfigurationId.from(solverConfigurationId);
        return configurationQuery.exists(typedId, username);
    }

    public void delete(@Nonnull String solverConfigurationId) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        SolverConfigurationId typedId = new SolverConfigurationId(solverConfigurationId);
        publisher.publishEvent(new DeleteSolverConfigurationCommand(typedId, username));
    }
}
