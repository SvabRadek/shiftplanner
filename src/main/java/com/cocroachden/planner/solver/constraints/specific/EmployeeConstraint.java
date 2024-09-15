package com.cocroachden.planner.solver.constraints.specific;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.SolverConstraint;

public interface EmployeeConstraint extends SolverConstraint {
    EmployeeId getOwner();
}
