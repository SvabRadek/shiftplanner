package com.cocroachden.planner.employee.endpoint;

import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.command.deleteemployee.DeleteEmployeeCommand;
import com.cocroachden.planner.employee.command.saveemployee.SaveEmployeeCommand;
import com.cocroachden.planner.employee.query.EmployeeQuery;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.security.Role;
import com.vaadin.flow.server.VaadinRequest;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import dev.hilla.Nullable;
import dev.hilla.crud.CrudRepositoryService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@BrowserCallable
@AllArgsConstructor
@RolesAllowed(Role.ROLE_USER)
public class EmployeeEndpoint extends CrudRepositoryService<EmployeeRecord, String, EmployeeRepository> {
    private EmployeeQuery employeeQuery;
    private ApplicationEventPublisher publisher;

    @Override
    public @Nullable EmployeeRecord save(EmployeeRecord value) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        publisher.publishEvent(
                new SaveEmployeeCommand(
                        value.getId(),
                        value.getFirstName(),
                        value.getLastName(),
                        username
                )
        );
        return employeeQuery.getById(value.getId(), username);
    }

    @Override
    public void delete(@Nonnull String employeeId) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        publisher.publishEvent(new DeleteEmployeeCommand(new EmployeeId(employeeId), username));
    }

    @Nonnull
    public List<@Nonnull EmployeeDTO> getAllEmployees() {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        return employeeQuery.findAll(username);
    }

    @Nonnull
    public List<@Nonnull EmployeeDTO> getEmployeesExcluding(@Nonnull List<@Nonnull String> employeeIds) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        var boxedIds = employeeIds.stream().map(EmployeeId::new).toList();
        return employeeQuery.allExceptTheseIds(boxedIds, username);
    }
}


