package com.cocroachden.planner.solver;

import java.io.Serializable;
import java.util.UUID;

public record SolverSubscriptionId(
        String id
) implements Serializable {

    public static SolverSubscriptionId random() {
        return new SolverSubscriptionId(UUID.randomUUID().toString());
    }

    public static SolverSubscriptionId from(String id) {
        return new SolverSubscriptionId(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SolverSubscriptionId other) {
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
