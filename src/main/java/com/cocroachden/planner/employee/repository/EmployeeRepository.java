package com.cocroachden.planner.employee.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeRecord, String>, JpaSpecificationExecutor<EmployeeRecord> {

  List<EmployeeRecord> findByIdInAndUsername(Collection<String> ids, String username);

  Boolean existsByFirstNameAndLastNameAndUsername(String firstName, String lastName, String username);

  List<EmployeeRecord> findAllByIdNotInAndUsername(Collection<String> ids, String username);
  List<EmployeeRecord> findAllByIdInAndUsername(Collection<String> ids, String username);
  List<EmployeeRecord> findAllByUsername(String username);
  Boolean existsByIdAndUsername(String id, String username);
  void deleteByIdAndUsername(String id, String username);
  EmployeeRecord getByIdAndUsername(String id, String username);
}
