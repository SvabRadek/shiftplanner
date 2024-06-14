package com.cocroachden.planner;


import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.cocroachden.planner.solver.api.WorkShifts.*;

public class DefaultSolverConfiguration {
  private static final LocalDate FIRST_DAY = LocalDate.of(2023, 11, 1);
  private static final LocalDate LAST_DAY = LocalDate.of(2023, 11, 30);

  public static List<EmployeeId> employees() {
    return IntStream.rangeClosed(1, 18).asLongStream()
        .mapToObj(EmployeeId::new)
        .toList();
  }

  public static List<ConstraintRequest> constraintRequests() {
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
    return allRequests;
  }
}
