package com.cocroachden.planner.employee.query;

import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeQuery {
    private final EmployeeRepository employeeRepository;

    public EmployeeRecord getById(EmployeeId id, String currentUser) {
        return employeeRepository.getByIdAndOwningUser(id.getId(), currentUser);
    }

    public List<EmployeeDTO> findAll(String currentUser) {
        return employeeRepository.findAllByOwningUser(currentUser).stream()
                .map(EmployeeDTO::from)
                .toList();
    }

    public Boolean existsByName(String firstName, String lastName, String currentUser) {
        return employeeRepository.existsByFirstNameAndLastNameAndOwningUser(firstName, lastName, currentUser);
    }

    public List<EmployeeDTO> findAllByIds(List<EmployeeId> ids, String currentUser) {
        var unboxedIds = ids.stream().map(EmployeeId::getId).toList();
        return employeeRepository.findAllByIdInAndOwningUser(unboxedIds, currentUser).stream()
                .map(EmployeeDTO::from)
                .toList();
    }

    public List<EmployeeDTO> allExceptTheseIds(List<EmployeeId> ids, String currentUser) {
        var unboxedIds = ids.stream().map(EmployeeId::getId).toList();
        return employeeRepository.findAllByIdNotInAndOwningUser(unboxedIds, currentUser).stream()
                .map(EmployeeDTO::from)
                .toList();
    }
}
