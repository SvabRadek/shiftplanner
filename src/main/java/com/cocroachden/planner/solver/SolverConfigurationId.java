package com.cocroachden.planner.solver;

import com.cocroachden.planner.common.AbstractIdentity;

public class SolverConfigurationId extends AbstractIdentity {
    public SolverConfigurationId(String id) {
        super(id);
    }

    public SolverConfigurationId() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SolverConfigurationId other) {
            return other.id.equals(id);
        }
        return false;
    }
}
