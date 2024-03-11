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
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static com.cocroachden.planner.solver.schedule.WorkShifts.*;

public class Example {
  private static final LocalDate FIRST_DAY = LocalDate.of(2023, 11, 1);
  private static final LocalDate LAST_DAY = LocalDate.of(2023, 11, 30);

  public static List<WorkerId> workers() {
    return IntStream.rangeClosed(1, 18).asLongStream()
        .mapToObj(WorkerId::new)
        .toList();
  }

  public static List<ConstraintRequest> constraintRequests() {
    var allRequests = new ArrayList<ConstraintRequest>();
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(1L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(2L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(3L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(4L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(5L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(6L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(7L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(8L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(9L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(10L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(11L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(12L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(13L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(14L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(15L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(16L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(17L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId(18L), 13, 14, 1, 14, 1, 15));
    allRequests.add(new WorkersPerShiftRequest(DAY, 4, 4, 0, 4, 0, 4));
    allRequests.add(new WorkersPerShiftRequest(NIGHT, 3, 3, 0, 3, 0, 3));
    allRequests.add(new ShiftFollowUpRestrictionRequest(NIGHT, DAY, 0));
    allRequests.add(new OneShiftPerDayRequest());
    allRequests.add(new ConsecutiveWorkingDaysRequest(WORKING_SHIFTS, 0, 2, 1, 3, 50, 3));
    return allRequests;
  }
}
