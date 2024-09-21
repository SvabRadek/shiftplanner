package com.cocroachden.planner.employee.query;

import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.vaadin.flow.server.VaadinRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class EmployeeQuery {
    private final EmployeeRepository employeeRepository;

    public EmployeeRecord getById(EmployeeId id, String currentUser) {
        return employeeRepository.getByIdAndUsername(id.getId(), currentUser);
    }

    public List<EmployeeDTO> findAll(String currentUser) {
        return employeeRepository.findAllByUsername(currentUser).stream()
                .map(EmployeeDTO::from)
                .toList();
    }

    public Boolean existsByName(String firstName, String lastName, String currentUser) {
        return employeeRepository.existsByFirstNameAndLastNameAndUsername(firstName, lastName, currentUser);
    }

    public List<EmployeeDTO> findAllByIds(List<EmployeeId> ids, String currentUser) {
        var unboxedIds = ids.stream().map(EmployeeId::getId).toList();
        return employeeRepository.findAllByIdInAndUsername(unboxedIds, currentUser).stream()
                .map(EmployeeDTO::from)
                .toList();
    }

    public List<EmployeeDTO> allExceptTheseIds(List<EmployeeId> ids, String currentUser) {
        var unboxedIds = ids.stream().map(EmployeeId::getId).toList();
        return employeeRepository.findAllByIdNotInAndUsername(unboxedIds, currentUser).stream()
                .map(EmployeeDTO::from)
                .toList();
    }
}
