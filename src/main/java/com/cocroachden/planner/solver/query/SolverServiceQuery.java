package com.cocroachden.planner.solver.query;

import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.SolverTask;
import com.cocroachden.planner.solver.service.SolverService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
public class SolverServiceQuery {
    private SolverService solverService;

    public Optional<SolverTask> findSolverTask(SolverSubscriptionId subscriptionId) {
        if (solverService.getSubscriptions().containsKey(subscriptionId)) {
            return Optional.of(solverService.getSubscriptions().get(subscriptionId));
        }
        return Optional.empty();
    }
}
