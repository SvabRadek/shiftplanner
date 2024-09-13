package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentId;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeAssignmentRepository extends JpaRepository<EmployeeAssignmentRecord, EmployeeAssignmentId> {
}
