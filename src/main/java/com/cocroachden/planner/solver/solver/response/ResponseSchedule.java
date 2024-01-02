package com.cocroachden.planner.solver.solver.response;

import com.cockroachden.shiftschedulerback.lib.WorkerId;

import java.util.List;
import java.util.Map;

public record ResponseSchedule(
    Map<WorkerId, List<ResponseWorkDay>> workdays

) {
}

