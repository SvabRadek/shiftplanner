package com.cocroachden.planner.solverconfiguration.service;

import com.cocroachden.planner.AbstractMessagingTest;
import com.cocroachden.planner.constraint.ConstraintId;
import com.cocroachden.planner.constraint.EmployeesPerShiftConstraintDTO;
import com.cocroachden.planner.constraint.TeamAssignmentConstraintDTO;
import com.cocroachden.planner.constraint.repository.ConstraintRepository;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.command.saveemployee.SaveEmployeeCommand;
import com.cocroachden.planner.employee.query.EmployeeQuery;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentId;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.SolverConfigurationHasBeenDeleted;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SolverConfigurationHasBeenSaved;
import com.cocroachden.planner.solverconfiguration.command.updateconfiguration.SolverConfigurationHasBeenUpdated;
import com.cocroachden.planner.solverconfiguration.command.updateconfiguration.UpdateSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRepository;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

class SolverConfigurationServiceTest extends AbstractMessagingTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EmployeeAssignmentRepository assignmentRepository;

    @Autowired
    private SolverConfigurationRepository configurationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ConstraintRepository constraintRepository;

    @Autowired
    private EmployeeAssignmentRepository employeeAssignmentRepository;

    @Autowired
    private EmployeeQuery employeeQuery;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        configurationRepository.deleteAll();
    }

    @Test
    public void itCanSaveSolverConfiguration() {
        var testConfigurationName = "Test Configuration";
        var id = new SolverConfigurationId("config-employeeId");
        var command = new SaveSolverConfigurationCommand(
                id,
                testConfigurationName,
                "irrelevant",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(SolverConfigurationHasBeenSaved.class);
        var savedConfig = configurationRepository.findById(command.id().getId()).orElseThrow();
        Assertions.assertThat(savedConfig.getName()).isEqualTo(testConfigurationName);
    }

    @Test
    public void itThrowsWhenSavingDuplicateSolverConfiguration() {
        var command = new SaveSolverConfigurationCommand(
                new SolverConfigurationId("config-employeeId"),
                "Test Configuration",
                "irrelevant",
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
                new SolverConfigurationId("config-employeeId"),
                "Test Configuration",
                "irrelevant",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(new EmployeeAssignmentDTO("non-existent-employeeId", 1, 10)),
                List.of()
        );
        this.thenCommandThrowsException(command, EntityNotFoundException.class);
    }

    @Test
    public void itCanDeleteSolverConfiguration() {
        var id = new SolverConfigurationId("config-employeeId");
        var saveCommand = new SaveSolverConfigurationCommand(
                id,
                "Test Configuration",
                "test-user",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.givenCommandHasBeenSent(saveCommand);
        var testedCommand = new DeleteSolverConfigurationCommand(id, "test-user");
        this.whenCommandHasBeenSent(testedCommand);
        this.thenExactlyOneEventHasBeenDispatched(SolverConfigurationHasBeenDeleted.class);
        Assertions.assertThat(configurationRepository.existsById(id.getId())).isFalse();
    }

    @Test
    public void itIgnoresWhenDeletingNonExistingSolverConfiguration() {
        var deleteCommand = new DeleteSolverConfigurationCommand(new SolverConfigurationId(
                "non-existent-config-employeeId"), "test-user");
        this.whenCommandHasBeenSent(deleteCommand);
        this.thenNoEventsOfTypeHaveBeenDispatched(SolverConfigurationHasBeenDeleted.class);
    }

    @Test
    public void itCanUpdateSolverConfiguration() {
        SolverConfigurationId id = new SolverConfigurationId("config-employeeId");
        var saveCommand = new SaveSolverConfigurationCommand(
                id,
                "Test Configuration",
                "irrelevant",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.givenCommandHasBeenSent(saveCommand);
        var updateCommand = new UpdateSolverConfigurationCommand(
                id,
                "Updated Configuration",
                "irrelevant",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                List.of(),
                List.of()
        );
        this.whenCommandHasBeenSent(updateCommand);
        var updatedConfigId = this
                .thenExactlyOneEventHasBeenDispatched(SolverConfigurationHasBeenUpdated.class)
                .configurationId();
        Assertions.assertThat(updatedConfigId).isEqualTo(updateCommand.id());
        var updated = configurationRepository.findById(id.getId()).orElseThrow();
        Assertions.assertThat(updated.getName()).isEqualTo("Updated Configuration");
    }

    @Test
    public void itThrowsWhenUpdatingNonExistingSolverConfiguration() {
        var updateCommand = new UpdateSolverConfigurationCommand(
                new SolverConfigurationId("non-existent-config-employeeId"),
                "Non-existent Configuration",
                "irrelevant",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.thenCommandThrowsException(updateCommand, IllegalArgumentException.class);
    }

    @Test
    public void itRemovesOrphanedConstraintsAfterConfigurationHasBeenRemoved() {
        SolverConfigurationId configurationId = new SolverConfigurationId("config-employeeId");
        ConstraintId constraintId = new ConstraintId("constraint-employeeId");
        var saveConfigCommand = new SaveSolverConfigurationCommand(
                configurationId,
                "Test Configuraiton",
                "test-user",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of(new EmployeesPerShiftConstraintDTO(
                        constraintId.getId(), WorkShifts.DAY, 1, 1, 1, 1, 1, 1
                ))
        );
        this.givenCommandHasBeenSent(saveConfigCommand);
        Assertions.assertThat(this.constraintRepository.existsById(constraintId.getId())).isTrue();
        var testedCommand = new DeleteSolverConfigurationCommand(configurationId, "test-user");
        this.whenCommandHasBeenSent(testedCommand);
        Assertions.assertThat(this.constraintRepository.existsById(constraintId.getId())).isFalse();
    }

    @Test
    @Transactional
    public void itCanSaveConfigurationWithAssignmentsAndConstraints() {
        var configurationId = SolverConfigurationId.random();
        var employeeId = EmployeeId.random();
        var constraintId = ConstraintId.random();
        var saveEmployeeCommand = new SaveEmployeeCommand(
                employeeId,
                "John",
                "Doe",
                "test-user"
        );
        this.givenCommandHasBeenSent(saveEmployeeCommand);
        var saveConfigCommand = new SaveSolverConfigurationCommand(
                configurationId,
                "Test Configuration",
                "test-user",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(new EmployeeAssignmentDTO(employeeId.getId(), 0, 1)),
                List.of(new TeamAssignmentConstraintDTO(
                        constraintId.getId(),
                        employeeId.getId(),
                        0,
                        true,
                        1
                ))
        );
        this.whenCommandHasBeenSent(saveConfigCommand);
        this.thenExactlyOneEventHasBeenDispatched(SolverConfigurationHasBeenSaved.class);
        var savedConfiguration = configurationRepository.findById(configurationId.getId()).orElseThrow();
        var savedEmployee = employeeRepository.findById(employeeId.getId()).orElseThrow();
        Assertions.assertThat(savedConfiguration.getConstraintRecords()).hasSize(1);
        Assertions.assertThat(savedEmployee.getConstraints()).hasSize(1);
        Assertions.assertThat(savedConfiguration.getEmployeeAssignments()).hasSize(1);
        Assertions.assertThat(savedEmployee.getAssignments()).hasSize(1);
    }

    @Test
    @Transactional
    public void itRemovesOrphanedEmployeeAssignmentsWhenConfigHasBeenRemoved() {
        var configurationId = SolverConfigurationId.random();
        var employeeId = EmployeeId.random();
        var saveEmployeeCommand = new SaveEmployeeCommand(
                employeeId,
                "John",
                "Doe",
                "test-user"
        );
        this.givenCommandHasBeenSent(saveEmployeeCommand);
        var saveConfigCommand = new SaveSolverConfigurationCommand(
                configurationId,
                "Test Configuration",
                "test-user",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(new EmployeeAssignmentDTO(employeeId.getId(), 0, 1)),
                List.of()
        );
        this.givenCommandHasBeenSent(saveConfigCommand);
        var testedCommand = new DeleteSolverConfigurationCommand(configurationId, "test-user");
        this.whenCommandHasBeenSent(testedCommand);
        this.thenExactlyOneEventHasBeenDispatched(SolverConfigurationHasBeenDeleted.class);
        Assertions.assertThat(
                employeeAssignmentRepository.findById(new EmployeeAssignmentId(employeeId.getId(), configurationId.getId()))
        ).isEmpty();
        Assertions.assertThat(
                configurationRepository.existsById(configurationId.getId())
        ).isFalse();
        Assertions.assertThat(
                employeeRepository.getByIdAndOwningUser(employeeId.getId(), "test-user").getAssignments()
        ).hasSize(0);
    }
}
