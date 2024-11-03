package com.cocroachden.planner.constraint.query;

import com.cocroachden.planner.constraint.ConstraintId;
import com.cocroachden.planner.constraint.ConstraintDTO;
import com.cocroachden.planner.constraint.mapping.ConstraintMapper;
import com.cocroachden.planner.constraint.repository.ConstraintRepository;
import com.cocroachden.planner.employee.EmployeeId;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@Transactional
public class ConstraintQuery {
    private final ConstraintRepository repository;

    public List<ConstraintDTO> getAllRequestsRelatedToEmployee(EmployeeId employeeId) {
        return repository.findByOwner_Id(employeeId).stream()
                .map(ConstraintMapper::fromRecord)
                .toList();
    }

    public <T extends ConstraintDTO> List<T> findSpecificById(List<ConstraintId> ids, Class<T> type) {
        return repository.findByIdIn(ids.stream().map(ConstraintId::getId).toList()).stream()
                .map(ConstraintMapper::fromRecord)
                .map(type::cast)
                .toList();
    }

    public List<ConstraintDTO> findByIds(List<ConstraintId> ids) {
        return StreamSupport.stream(repository.findAllById(ids.stream().map(ConstraintId::getId).toList()).spliterator(), false)
                .map(ConstraintMapper::fromRecord)
                .toList();
    }

}
