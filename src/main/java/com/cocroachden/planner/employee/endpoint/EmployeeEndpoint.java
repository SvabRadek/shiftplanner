package com.cocroachden.planner.employee.endpoint;

import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.command.deleteemployee.DeleteEmployeeCommand;
import com.cocroachden.planner.employee.query.EmployeeQuery;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import dev.hilla.crud.CrudRepositoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class EmployeeEndpoint extends CrudRepositoryService<EmployeeRecord, EmployeeId, EmployeeRepository> {
    private EmployeeQuery employeeQuery;
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void delete(@Nonnull EmployeeId employeeId) {
        publisher.publishEvent(new DeleteEmployeeCommand(employeeId));
    }

    @Nonnull
    public List<@Nonnull EmployeeDTO> getAllEmployees() {
        return employeeQuery.findAll();
    }

    @Nonnull
    public List<@Nonnull EmployeeDTO> getEmployeesExcluding(@Nonnull List<@Nonnull String> employeeIds) {
        return employeeQuery.allExceptTheseIds(employeeIds.stream().map(EmployeeId::new).toList());
    }
}


