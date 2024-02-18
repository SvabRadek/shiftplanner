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

  public static SchedulePlanConfiguration createExampleConfiguration() {
    return new SchedulePlanConfiguration(
        FIRST_DAY,
        LAST_DAY,
        List.of(
            new ScheduleWorker(new WorkerId("0"), 5),
            new ScheduleWorker(new WorkerId("1"), 1),
            new ScheduleWorker(new WorkerId("2"), 1),
            new ScheduleWorker(new WorkerId("3"), 1),
            new ScheduleWorker(new WorkerId("4"), 1),
            new ScheduleWorker(new WorkerId("5"), 1),
            new ScheduleWorker(new WorkerId("6"), 1),
            new ScheduleWorker(new WorkerId("7"), 1),
            new ScheduleWorker(new WorkerId("8"), 1),
            new ScheduleWorker(new WorkerId("9"), 1),
            new ScheduleWorker(new WorkerId("10"), 1),
            new ScheduleWorker(new WorkerId("11"), 1),
            new ScheduleWorker(new WorkerId("12"), 1),
            new ScheduleWorker(new WorkerId("13"), 1),
            new ScheduleWorker(new WorkerId("14"), 1),
            new ScheduleWorker(new WorkerId("15"), 1),
            new ScheduleWorker(new WorkerId("16"), 1),
            new ScheduleWorker(new WorkerId("17"), 1)
        )
    );
  }

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

  public static List<ScheduleRequest> scheduleRequests() {
    var scheduleRequests = new ArrayList<ScheduleRequest>();
    return scheduleRequests;
  }

  public static List<ConstraintRequest> constraintRequests() {
    var allRequests = new ArrayList<ConstraintRequest>();
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("0"), 11, 13, 1, 13, 1, 15));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("1"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("2"), 5, 11, 1, 11, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("3"), 5, 10, 1, 10, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("4"), 5, 11, 1, 11, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("5"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("6"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("7"), 5, 6, 1, 6, 1, 8));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("8"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("9"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("10"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("11"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("12"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("13"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("14"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("15"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("16"), 10, 12, 1, 12, 1, 14));
    allRequests.add(new ShiftsPerScheduleRequest(new WorkerId("17"), 10, 12, 1, 12, 1, 14));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("0"), FIRST_DAY, DAY, DAY, OFF, NIGHT));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("1"), FIRST_DAY, DAY, OFF, OFF, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("2"), FIRST_DAY, OFF, OFF, OFF, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("3"), FIRST_DAY, DAY, DAY, NIGHT, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("4"), FIRST_DAY, NIGHT, NIGHT, OFF, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("5"), FIRST_DAY, OFF, OFF, OFF, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("6"), FIRST_DAY, OFF, OFF, DAY, DAY));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("7"), FIRST_DAY, OFF, OFF, DAY, DAY));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("8"), FIRST_DAY, OFF, NIGHT, OFF, NIGHT));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("9"), FIRST_DAY, NIGHT, NIGHT, OFF, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("10"), FIRST_DAY, OFF, OFF, OFF, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("11"), FIRST_DAY, OFF, DAY, OFF, DAY));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("12"), FIRST_DAY, OFF, OFF, OFF, NIGHT));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("13"), FIRST_DAY, OFF, OFF, OFF, NIGHT));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("14"), FIRST_DAY, OFF, OFF, OFF, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("15"), FIRST_DAY, OFF, OFF, DAY, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("16"), FIRST_DAY, OFF, OFF, DAY, OFF));
//    allRequests.addAll(createSpecificShiftRequestSequence(new WorkerId("17"), FIRST_DAY, NIGHT, NIGHT, NIGHT, OFF));
    allRequests.add(new WorkersPerShiftRequest(DAY, 4, 4, 0, 4, 0, 4));
    allRequests.add(new WorkersPerShiftRequest(NIGHT, 3, 3, 0, 3, 0, 3));
    allRequests.add(new ShiftFollowUpRestrictionRequest(NIGHT, DAY, 0));
    allRequests.add(new OneShiftPerDayRequest());
    allRequests.add(new ConsecutiveWorkingDaysRequest(WORKING_SHIFTS, 0, 2, 3, 3, 10, 4));
    allRequests.add(new ShiftPatternConstraintRequest(new WorkerId("0"), 1, Example.apolinarPattern2()));
    return allRequests;
  }

  private static WorkShifts[] classicPattern() {
    return new WorkShifts[]{
        OFF,
        NIGHT,
        NIGHT,
        NIGHT,
        OFF,
        OFF,
        DAY,
        DAY,
        OFF
    };
  }

  private static WorkShifts[] classicPattern2() {
    return new WorkShifts[]{
        OFF,
        DAY,
        DAY,
        DAY,
        OFF,
        OFF,
        NIGHT,
        NIGHT,
        OFF
    };
  }

  private static WorkShifts[] apolinarPattern() {
    return new WorkShifts[]{
        OFF,
        DAY,
        DAY,
        NIGHT,
        OFF,
        OFF,
        DAY,
        NIGHT,
        OFF
    };
  }

  private static WorkShifts[] apolinarPattern2() {
    return new WorkShifts[]{
        OFF,
        DAY,
        NIGHT,
        NIGHT,
        OFF,
        OFF,
        DAY,
        NIGHT,
        OFF
    };
  }

  private static List<ConstraintRequest> createSpecificShiftRequestSequence(
      WorkerId workerId,
      LocalDate startDay,
      WorkShifts... sequence
  ) {
    var list = new ArrayList<ConstraintRequest>();
    for (int i = 0; i < sequence.length; i++) {
      list.add(new SpecificShiftRequest(workerId, startDay.plusDays(i), sequence[i]));
    }
    return list;
  }
}
