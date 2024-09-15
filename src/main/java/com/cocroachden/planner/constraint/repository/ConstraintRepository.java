package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.employee.EmployeeId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface ConstraintRepository extends CrudRepository<ConstraintRecord, String>, JpaSpecificationExecutor<ConstraintRecord> {
  List<ConstraintRecord> findByIdIn(Collection<String> ids);
  List<ConstraintRecord> findByOwner_Id(EmployeeId id);

}
