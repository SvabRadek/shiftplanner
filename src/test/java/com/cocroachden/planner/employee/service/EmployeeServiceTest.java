package com.cocroachden.planner.employee.service;

import com.cocroachden.AbstractMessagingTest;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.command.deleteemployee.DeleteEmployeeCommand;
import com.cocroachden.planner.employee.command.deleteemployee.EmployeeHasBeenDeleted;
import com.cocroachden.planner.employee.command.saveemployee.EmployeeHasBeenSaved;
import com.cocroachden.planner.employee.command.saveemployee.SaveEmployeeCommand;
import com.cocroachden.planner.employee.query.EmployeeQuery;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmployeeServiceTest extends AbstractMessagingTest {

    @Autowired
    private EmployeeQuery employeeQuery;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    public void itCanSaveNewEmployee() {
        var id = EmployeeId.random();
        var command = new SaveEmployeeCommand(id,"John","Doe", "test-user");
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(EmployeeHasBeenSaved.class);
        Assertions.assertThat(employeeRepository.findById(id.getId())).isPresent();
    }

    @Test
    public void itThrowsWhenSavingExistingEmployee() {
        var id = EmployeeId.random();
        var command = new SaveEmployeeCommand(id,"John","Doe", "test-user");
        this.givenCommandHasBeenSent(command);
        this.thenCommandThrowsException(command, IllegalArgumentException.class);
    }

    @Test
    public void itCanDeleteEmployee() {
        var id = EmployeeId.random();
        var saveCommand = new SaveEmployeeCommand(id,"John","Doe", "test-user");
        this.givenCommandHasBeenSent(saveCommand);
        var deleteCommand = new DeleteEmployeeCommand(id, "test-user");
        this.whenCommandHasBeenSent(deleteCommand);
        this.thenExactlyOneEventHasBeenDispatched(EmployeeHasBeenDeleted.class);
        Assertions.assertThat(employeeRepository.existsById(id.getId())).isFalse();
    }

    @Test
    public void itIgnoresWhenDeletingNonExistingEmployee() {
        var employeeId = new EmployeeId("non-existent-employeeId");
        var deleteCommand = new DeleteEmployeeCommand(employeeId, "test-user");
        this.whenCommandHasBeenSent(deleteCommand);
        this.thenNoEventsOfTypeHaveBeenDispatched(EmployeeHasBeenDeleted.class);
    }
}
