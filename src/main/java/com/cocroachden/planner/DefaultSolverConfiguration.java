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
        new EmployeeRecord(1L, "Martina", "Kravariková"),
        new EmployeeRecord(2L, "Vendula", "Zajícová"),
        new EmployeeRecord(3L, "Alena", "Janáková"),
        new EmployeeRecord(4L, "Simona", "Holmanová"),
        new EmployeeRecord(5L, "Jana", "Zelenková"),
        new EmployeeRecord(6L, "Jolana", "Pálffyová"),
        new EmployeeRecord(7L, "Nicola", "Halbichová"),
        new EmployeeRecord(8L, "Jana", "Kesslerová"),
        new EmployeeRecord(9L, "Eva", "Dudek Premauer"),
        new EmployeeRecord(10L, "Aneta", "Dubská"),
        new EmployeeRecord(11L, "Jindra", "Labounková"),
        new EmployeeRecord(12L, "Dana", "Zachová"),
        new EmployeeRecord(13L, "Iva", "Najmanová"),
        new EmployeeRecord(14L, "Barbora", "Řeháková"),
        new EmployeeRecord(15L, "Karolína", "Vavrušková"),
        new EmployeeRecord(16L, "Zuzana", "Kučerová"),
        new EmployeeRecord(17L, "Natálie", "Vejvodová"),
        new EmployeeRecord(18L, "Karolína", "Hromířová")
    );
  }

}
