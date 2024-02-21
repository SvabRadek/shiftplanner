package com.cocroachden.planner;


import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.SchedulePlanConfiguration;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;
import com.cocroachden.planner.solver.schedule.ScheduleWorker;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.cocroachden.planner.solver.schedule.request.ScheduleRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.cocroachden.planner.solver.schedule.WorkShifts.*;

public class Example {
  private static final LocalDate FIRST_DAY = LocalDate.of(2023, 11, 1);
  private static final LocalDate LAST_DAY = LocalDate.of(2023, 11, 30);

  public static List<WorkerId> workers() {
    return List.of(
        new WorkerId("0"),
        new WorkerId("1"),
        new WorkerId("2"),
        new WorkerId("3"),
        new WorkerId("4"),
        new WorkerId("5"),
        new WorkerId("6"),
        new WorkerId("7"),
        new WorkerId("8"),
        new WorkerId("9"),
        new WorkerId("10"),
        new WorkerId("11"),
        new WorkerId("12"),
        new WorkerId("13"),
        new WorkerId("14"),
        new WorkerId("15"),
        new WorkerId("16"),
        new WorkerId("17")
    );
  }

  public static List<ConstraintRequest> constraintRequests() {
    var allRequests = new ArrayList<ConstraintRequest>();
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("0"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("1"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("2"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("3"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("4"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("5"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("6"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("7"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("8"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("9"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("10"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("11"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("12"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("13"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("14"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("15"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("16"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("17"), 13, 14, 1, 14, 1, 15));
    allRequests.add(new WorkersPerShiftRequest(DAY, 4, 4, 0, 4, 0, 4));
    allRequests.add(new WorkersPerShiftRequest(NIGHT, 3, 3, 0, 3, 0, 3));
    allRequests.add(new ShiftFollowUpRestrictionRequest(NIGHT, DAY, 0));
    allRequests.add(new OneShiftPerDayRequest());
    allRequests.add(new ConsecutiveWorkingDaysRequest(WORKING_SHIFTS, 0, 2, 1, 3, 50, 3));
    return allRequests;
  }
}
