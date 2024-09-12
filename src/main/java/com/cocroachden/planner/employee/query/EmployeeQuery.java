package com.cocroachden.planner.employee.query;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class EmployeeQuery {
    private final EmployeeRepository employeeRepository;

    public Boolean existsByName(String firstName, String lastName) {
        return employeeRepository.exists(hasName(firstName, lastName));
    }

    public List<EmployeeDTO> findAllByIds(List<EmployeeId> ids) {
        return StreamSupport.stream(employeeRepository.findAllById(ids).spliterator(), false)
                .map(EmployeeDTO::from)
                .toList();
    }

    public List<EmployeeDTO> allExceptTheseIds(List<EmployeeId> ids) {
        var stringIds = ids.stream().map(EmployeeId::getId).toList();
        return employeeRepository.findAll(
                (root, query, criteriaBuilder) -> criteriaBuilder.not(root.get("id")).in(stringIds)
                ).stream()
                .map(EmployeeDTO::from)
                .toList();
    }


    private static Specification<EmployeeRecord> hasName(String firstName, String lastName) {
        return (root, query, criteriaBuilder) -> {
            var firstNamePredicate = criteriaBuilder.equal(root.get("firstName"), firstName);
            var lastNamePredicate = criteriaBuilder.equal(root.get("lastName"), lastName);
            return criteriaBuilder.and(firstNamePredicate, lastNamePredicate);
        };

    }

}
