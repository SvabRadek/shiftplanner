package com.cocroachden.planner.solver.solver.response;


import com.cocroachden.planner.lib.WorkerId;

import java.util.List;
import java.util.Map;

public record ResponseSchedule(
    Map<WorkerId, List<ResponseWorkDay>> workdays

) {
}

