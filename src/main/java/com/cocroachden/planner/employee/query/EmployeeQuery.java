package com.cocroachden.planner.employee.query;

import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class EmployeeQuery {
    private final EmployeeRepository employeeRepository;

    public List<EmployeeDTO> findAll() {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), true)
                .map(EmployeeDTO::from)
                .toList();
    }

    public Boolean existsByName(String firstName, String lastName) {
        return employeeRepository.existsByFirstNameAndLastName(firstName, lastName);
    }

    public List<EmployeeDTO> findAllByIds(List<EmployeeId> ids) {
        return StreamSupport.stream(employeeRepository.findAllById(ids).spliterator(), false)
                .map(EmployeeDTO::from)
                .toList();
    }

    public List<EmployeeDTO> allExceptTheseIds(List<EmployeeId> ids) {
        return employeeRepository.findAllByIdNotIn(ids).stream().map(EmployeeDTO::from).toList();
    }
}
