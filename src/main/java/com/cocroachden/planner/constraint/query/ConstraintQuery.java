package com.cocroachden.planner.constraint.query;

import com.cocroachden.planner.constraint.service.ConstraintMapper;
import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.employee.EmployeeId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConstraintQuery {
    private final ConstraintRequestRepository repository;

    public List<ConstraintRequestDTO> getAllRequestsRelatedToEmployee(EmployeeId employeeId) {
        return repository.findByOwner_Id(employeeId).stream()
                .map(ConstraintMapper::fromRecord)
                .toList();
    }

}
