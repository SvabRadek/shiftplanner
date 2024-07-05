package com.cocroachden.planner;


import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.cocroachden.planner.solver.api.WorkShifts.*;

public class DefaultSolverConfiguration {
  private static final LocalDate FIRST_DAY = LocalDate.of(2023, 11, 1);
  private static final LocalDate LAST_DAY = LocalDate.of(2023, 11, 30);

  public static List<ConstraintRequestRecord> constraintRequests() {
    var allRequests = new ArrayList<ConstraintRequest>();
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(1L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(2L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(3L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(4L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(5L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(6L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(7L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(8L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(9L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(10L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(11L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(12L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(13L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(14L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(15L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(16L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(17L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId(18L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new EmployeesPerShiftRequest(DAY, 4, 4, 0, 4, 0, 4));
    allRequests.add(new EmployeesPerShiftRequest(NIGHT, 3, 3, 0, 3, 0, 3));
    allRequests.add(new ShiftFollowUpRestrictionRequest(NIGHT, DAY, 0));
    allRequests.add(new ConsecutiveWorkingDaysRequest(WORKING_SHIFTS, 0, 2, 1, 3, 50, 3));
    return allRequests.stream()
        .map(c -> {
          var record = new ConstraintRequestRecord();
          record.setType(c.getType());
          record.setRequest(c);
          return record;
        }).toList();
  }

  public static List<EmployeeRecord> employees() {
    return List.of(
        createEmployee("Martina", "Kravariková"),
        createEmployee("Vendula", "Zajícová"),
        createEmployee("Alena", "Janáková"),
        createEmployee("Simona", "Holmanová"),
        createEmployee("Jana", "Zelenková"),
        createEmployee("Jolana", "Pálffyová"),
        createEmployee("Nicola", "Halbichová"),
        createEmployee("Jana", "Kesslerová"),
        createEmployee("Eva", "Dudek Premauer"),
        createEmployee("Aneta", "Dubská"),
        createEmployee("Jindra", "Labounková"),
        createEmployee("Dana", "Zachová"),
        createEmployee("Iva", "Najmanová"),
        createEmployee("Barbora", "Řeháková"),
        createEmployee("Karolína", "Vavrušková"),
        createEmployee("Zuzana", "Kučerová"),
        createEmployee("Natálie", "Vejvodová"),
        createEmployee("Karolína", "Hromířová")
    );
  }

  private static EmployeeRecord createEmployee(String firstName, String lastName) {
    var record = new EmployeeRecord();
    record.setFirstName(firstName);
    record.setLastName(lastName);
    return record;
  }

}
