package com.cocroachden.planner.solver.solver.response;


import com.cocroachden.planner.solver.schedule.WorkShifts;

import java.util.ArrayList;

public class ResponseSchedulePrinter {
  public static String print(ScheduleResult scheduleResult) {
    var sb = new StringBuilder();
    scheduleResult.workdays()
        .forEach((workerId, responseWorkDays) -> {
          var assignmentCount = new ArrayList<Integer>();
          sb.append("Worker ").append(workerId).append(" ");
          responseWorkDays.forEach(workDay -> {
            var symbol = workDay.assignedShift().getSymbol();
            sb.append(" | ").append(symbol.equals("V") ? " " : symbol);
            if (!workDay.assignedShift().equals(WorkShifts.OFF)) {
              assignmentCount.add(1);
            }
          });
          sb.append(" Assignments: ").append(assignmentCount.size()).append(System.lineSeparator());
        });
    return sb.toString();
  }

}
