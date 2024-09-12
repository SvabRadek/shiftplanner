package com.cocroachden.planner.employee.repository;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.EmployeeRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface EmployeeRepository extends CrudRepository<EmployeeRecord, EmployeeId>, JpaSpecificationExecutor<EmployeeRecord> {

  List<EmployeeRecord> findByIdIn(Collection<EmployeeId> ids);

  Boolean existsByFirstNameAndLastName(String firstName, String lastName);

  List<EmployeeRecord> findAllByIdNotIn(Collection<EmployeeId> ids);
}
