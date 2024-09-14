package com.cocroachden.planner.solver.constraints.specific;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;

public interface EmployeeConstraint extends ConstraintRequest {
    EmployeeId getOwner();
}
