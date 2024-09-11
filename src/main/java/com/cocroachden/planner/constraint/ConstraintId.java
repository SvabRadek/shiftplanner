package com.cocroachden.planner.constraint;

import com.cocroachden.planner.common.AbstractIdentity;

import java.util.UUID;


public class ConstraintId extends AbstractIdentity {
    public static ConstraintId random() {
        return new ConstraintId(UUID.randomUUID().toString());
    }

    public ConstraintId(String id) {
        super(id);
    }

    protected ConstraintId() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ConstraintId other) {
            return other.id.equals(id);
        }
        return false;
    }
}
