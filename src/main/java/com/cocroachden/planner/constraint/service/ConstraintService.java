package com.cocroachden.planner.constraint.service;

import com.cocroachden.planner.constraint.command.deleteconstraint.ConstraintHasBeenDeleted;
import com.cocroachden.planner.constraint.command.deleteconstraint.DeleteConstraintCommand;
import com.cocroachden.planner.constraint.command.saveconstraint.ConstraintHasBeenSaved;
import com.cocroachden.planner.constraint.command.saveconstraint.SaveConstraintCommand;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConstraintService {

    private final ConstraintRequestRepository constraintRequestRepository;
    private final SolverConfigurationRepository solverConfigurationRepository;
    private final EmployeeRepository employeeRepository;

    @EventListener
    public ConstraintHasBeenSaved handle(SaveConstraintCommand command) {
        var constraint = ConstraintMapper.fromDto(command.constraint());
        var constraintRecord = new ConstraintRequestRecord(constraint);
        if (constraint instanceof AbstractEmployeeSpecificConstraint employeeSpecificConstraint) {
            employeeSpecificConstraint.getOwner()
                    .map(employeeId -> employeeRepository.findById(employeeId)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "While recovering owner of constraint, employee with id [%s] was not found! " +
                                            "This should never happen and perhaps employee has been deleted and constraint was not!"
                            ))
                    ).ifPresent(constraintRecord::setOwner);
        }
        solverConfigurationRepository.findById(command.solverConfigurationId()).ifPresent(constraintRecord::setParent);
        constraintRequestRepository.save(new ConstraintRequestRecord(constraint));
        return new ConstraintHasBeenSaved(constraintRecord);
    }

    @EventListener
    public ConstraintHasBeenDeleted handle(DeleteConstraintCommand command) {
        constraintRequestRepository.deleteById(command.constraintId());
        return new ConstraintHasBeenDeleted(command.constraintId());
    }

}
