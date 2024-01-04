package com.cocroachden.planner.employee;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<EmployeeRecord, Long> {
}
