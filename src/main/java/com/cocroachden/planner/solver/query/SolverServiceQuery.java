package com.cocroachden.planner.solver.query;

import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.SolverTask;
import com.cocroachden.planner.solver.service.SolverService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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

    public Optional<SolverSolutionDTO> findLatestSolution(SolverSubscriptionId subscriptionId) {
        var cachedSolutions = solverService.getCachedSolutions();
        if (!cachedSolutions.containsKey(subscriptionId)) {
            return Optional.empty();
        }
        var solutions = solverService.getCachedSolutions().get(subscriptionId);
        if (solutions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(solutions.getLast());
    }
}
