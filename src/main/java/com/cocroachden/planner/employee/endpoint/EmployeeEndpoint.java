package com.cocroachden.planner.employee.endpoint;

import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.EmployeeRecord;
import com.cocroachden.planner.employee.command.deleteemployee.DeleteEmployeeCommand;
import com.cocroachden.planner.employee.query.EmployeeQuery;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import dev.hilla.crud.CrudRepositoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class EmployeeEndpoint extends CrudRepositoryService<EmployeeRecord, EmployeeId, EmployeeRepository> {
    private EmployeeRepository employeeRepository;
    private SolverConfigurationRepository plannerConfigurationRepository;
    private EmployeeQuery employeeQuery;
    private ApplicationEventPublisher publisher;

    @Nonnull
    public List<@Nonnull EmployeeDTO> getAllEmployees() {
        return StreamSupport
                .stream(employeeRepository.findAll().spliterator(), false)
                .map(EmployeeDTO::from)
                .toList();
    }

    @Override
    @Transactional
    public void delete(@Nonnull EmployeeId employeeId) {
        publisher.publishEvent(new DeleteEmployeeCommand(employeeId));
    }

    @Nonnull
    public List<@Nonnull EmployeeRecord> getEmployeesExcluding(@Nonnull List<@Nonnull Long> employeeIds) {
        return employeeRepository.findByIdNotIn(employeeIds);
    }
}


