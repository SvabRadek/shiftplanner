package com.cocroachden.planner.employee.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeRecord, String>, JpaSpecificationExecutor<EmployeeRecord> {

  List<EmployeeRecord> findByIdInAndOwningUser(Collection<String> ids, String username);

  Boolean existsByFirstNameAndLastNameAndOwningUser(String firstName, String lastName, String username);
  Optional<EmployeeRecord> findByFirstNameAndLastNameAndOwningUser(String firstName, String lastName, String owningUser);
  List<EmployeeRecord> findAllByIdNotInAndOwningUser(Collection<String> ids, String username);
  List<EmployeeRecord> findAllByIdInAndOwningUser(Collection<String> ids, String username);
  List<EmployeeRecord> findAllByOwningUser(String username);
  Boolean existsByIdAndOwningUser(String id, String username);
  void deleteByIdAndOwningUser(String id, String username);
  EmployeeRecord getByIdAndOwningUser(String id, String username);
}
