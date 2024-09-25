package com.cocroachden.planner.solver.query;

import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.service.SolverService;
import com.cocroachden.planner.solver.service.solver.Solver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class SolverServiceQuery {
    private SolverService solverService;

    public Optional<Solver> findSolver(SolverSubscriptionId subscriptionId) {
        return Optional.ofNullable(solverService.getSubscriptions().get(subscriptionId));
    }
}
