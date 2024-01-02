package com.cocroachden.planner.solver.solver.response;

import com.cockroachden.shiftschedulerback.model.schedule.WorkShifts;

import java.util.ArrayList;

public class ResponseSchedulePrinter {
  public static String print(ResponseSchedule responseSchedule) {
    var sb = new StringBuilder();
    responseSchedule.workdays()
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
