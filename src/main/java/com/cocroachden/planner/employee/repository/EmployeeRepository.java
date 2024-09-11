package com.cocroachden.planner.employee.repository;

import com.cocroachden.planner.employee.EmployeeId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface EmployeeRepository extends CrudRepository<EmployeeRecord, EmployeeId>, JpaSpecificationExecutor<EmployeeRecord> {

  List<EmployeeRecord> findByIdNotIn(Collection<Long> ids);

  List<EmployeeRecord> findByIdIn(Collection<Long> ids);
}
