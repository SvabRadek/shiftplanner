package com.cocroachden.planner.employee.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface EmployeeRepository extends CrudRepository<EmployeeRecord, Long>, JpaSpecificationExecutor<EmployeeRecord> {

  List<EmployeeRecord> findByIdNotIn(Collection<Long> ids);
}
