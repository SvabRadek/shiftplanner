package com.cocroachden.planner.employee.endpoint;

import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.command.saveemployee.SaveEmployeeCommand;
import com.cocroachden.planner.employee.query.EmployeeQuery;
import com.cocroachden.planner.security.Role;
import com.vaadin.flow.server.VaadinRequest;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@BrowserCallable
@RolesAllowed({ Role.ROLE_USER })
@AllArgsConstructor
public class CustomEmployeeEndpoint {

    private final EmployeeQuery employeeQuery;
    private final ApplicationEventPublisher publisher;

    public @Nonnull String save(@Nonnull EmployeeDTO employeeDTO) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        var id = EmployeeId.random();
        publisher.publishEvent(
                new SaveEmployeeCommand(
                        id,
                        employeeDTO.getFirstName(),
                        employeeDTO.getLastName(),
                        username
                )
        );
        return id.getId();
    }

    @Nonnull
    public List<@Nonnull EmployeeDTO> getAllEmployees() {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        return employeeQuery.findAll(username);
    }

    public EmployeeDTO findByName(@Nonnull String firstName, @Nonnull String lastName) {
        var username = VaadinRequest.getCurrent().getUserPrincipal().getName();
        return employeeQuery.findByName(firstName, lastName, username).orElse(null);
    }
}
