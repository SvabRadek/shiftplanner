package com.cocroachden.planner.employee.repository;

import com.cocroachden.planner.employee.EmployeeId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeRecord, EmployeeId>, JpaSpecificationExecutor<EmployeeRecord> {

  List<EmployeeRecord> findByIdIn(Collection<EmployeeId> ids);

  Boolean existsByFirstNameAndLastName(String firstName, String lastName);

  List<EmployeeRecord> findAllByIdNotIn(Collection<EmployeeId> ids);
}
