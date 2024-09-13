package com.cocroachden.planner.solverconfiguration.service;

import com.cocroachden.AbstractMessagingTest;
import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.SolverConfigurationHasBeenDeleted;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SolverConfigurationHasBeenSaved;
import com.cocroachden.planner.solverconfiguration.command.updateconfiguration.SolverConfigurationHasBeenUpdated;
import com.cocroachden.planner.solverconfiguration.command.updateconfiguration.UpdateSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRepository;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

class SolverConfigurationServiceTest extends AbstractMessagingTest {

    @Autowired
    private EmployeeAssignmentRepository assignmentRepository;

    @Autowired
    private SolverConfigurationRepository configurationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        assignmentRepository.deleteAll();
        configurationRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    public void itCanSaveSolverConfiguration() {
        var testConfigurationName = "Test Configuration";
        var id = new SolverConfigurationId("config-id");
        var command = new SaveSolverConfigurationCommand(
                id,
                testConfigurationName,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(SolverConfigurationHasBeenSaved.class);
        var savedConfig = configurationRepository.findById(command.id()).orElseThrow();
        Assertions.assertThat(savedConfig.getName()).isEqualTo(testConfigurationName);
    }

    @Test
    public void itThrowsWhenSavingDuplicateSolverConfiguration() {
        var command = new SaveSolverConfigurationCommand(
                new SolverConfigurationId("config-id"),
                "Test Configuration",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.givenCommandHasBeenSent(command);
        this.thenCommandThrowsException(command, IllegalArgumentException.class);
    }

    @Test
    public void itThrowsWhenEmployeeAssignedDoesNotExist() {
        var command = new SaveSolverConfigurationCommand(
                new SolverConfigurationId("config-id"),
                "Test Configuration",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(new EmployeeAssignmentDTO(EmployeeId.from("non-existent-id"), 1, 10)),
                List.of()
        );
        this.thenCommandThrowsException(command, IllegalArgumentException.class);
    }

    @Test
    public void itCanDeleteSolverConfiguration() {
        var id = new SolverConfigurationId("config-id");
        var saveCommand = new SaveSolverConfigurationCommand(
                id,
                "Test Configuration",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.givenCommandHasBeenSent(saveCommand);
        var testedCommand = new DeleteSolverConfigurationCommand(id);
        this.whenCommandHasBeenSent(testedCommand);
        this.thenExactlyOneEventHasBeenDispatched(SolverConfigurationHasBeenDeleted.class);
        Assertions.assertThat(configurationRepository.existsById(id)).isFalse();
    }

    @Test
    public void itIgnoresWhenDeletingNonExistingSolverConfiguration() {
        var deleteCommand = new DeleteSolverConfigurationCommand(new SolverConfigurationId("non-existent-config-id"));
        this.whenCommandHasBeenSent(deleteCommand);
        this.thenNoEventsOfTypeHaveBeenDispatched(SolverConfigurationHasBeenDeleted.class);
    }

    @Test
    public void itCanUpdateSolverConfiguration() {
        SolverConfigurationId id = new SolverConfigurationId("config-id");
        var saveCommand = new SaveSolverConfigurationCommand(
                id,
                "Test Configuration",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.givenCommandHasBeenSent(saveCommand);
        var updateCommand = new UpdateSolverConfigurationCommand(
                id,
                "Updated Configuration",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                List.of(),
                List.of()
        );
        this.whenCommandHasBeenSent(updateCommand);
        var updatedConfig = this.thenExactlyOneEventHasBeenDispatched(SolverConfigurationHasBeenUpdated.class)
                .configurationRecord();
        Assertions.assertThat(updatedConfig.getName()).isEqualTo("Updated Configuration");
        Assertions.assertThat(updatedConfig.getStartDate()).isEqualTo(updateCommand.startDate());
        Assertions.assertThat(updatedConfig.getEndDate()).isEqualTo(updateCommand.endDate());
    }

    @Test
    public void itThrowsWhenUpdatingNonExistingSolverConfiguration() {
        var updateCommand = new UpdateSolverConfigurationCommand(
                new SolverConfigurationId("non-existent-config-id"),
                "Non-existent Configuration",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.thenCommandThrowsException(updateCommand, IllegalArgumentException.class);
    }
}
