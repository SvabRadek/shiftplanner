package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAssignmentRepository extends JpaRepository<EmployeeAssignmentRecord, EmployeeAssignmentId> {
}
