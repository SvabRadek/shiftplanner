package com.cocroachden.planner.constraint.query;

import com.cocroachden.planner.constraint.ConstraintId;
import com.cocroachden.planner.constraint.service.ConstraintMapper;
import com.cocroachden.planner.constraint.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.employee.EmployeeId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ConstraintQuery {
    private final ConstraintRequestRepository repository;

    public List<ConstraintRequestDTO> getAllRequestsRelatedToEmployee(EmployeeId employeeId) {
        return repository.findByOwner_Id(employeeId).stream()
                .map(ConstraintMapper::fromRecord)
                .toList();
    }

    public <T extends ConstraintRequestDTO> List<T> findSpecificById(List<ConstraintId> ids, Class<T> type) {
        return repository.findByIdIn(ids).stream()
                .map(ConstraintMapper::fromRecord)
                .map(type::cast)
                .toList();
    }

    public List<ConstraintRequestDTO> findByIds(List<ConstraintId> ids) {
        return StreamSupport.stream(repository.findAllById(ids).spliterator(), false)
                .map(ConstraintMapper::fromRecord)
                .toList();
    }

}
