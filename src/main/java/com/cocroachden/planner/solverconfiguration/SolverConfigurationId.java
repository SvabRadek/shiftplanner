package com.cocroachden.planner.solverconfiguration;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SolverConfigurationId implements Serializable {
    private String id;

    public static SolverConfigurationId random() {
        return new SolverConfigurationId(UUID.randomUUID().toString());
    }

    public static SolverConfigurationId from(String id) {
        return new SolverConfigurationId(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SolverConfigurationId other) {
            return other.id.equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
