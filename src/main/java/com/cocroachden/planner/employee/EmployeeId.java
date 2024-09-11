package com.cocroachden.planner.employee;

import com.cocroachden.planner.common.AbstractIdentity;

import java.util.UUID;

public class EmployeeId extends AbstractIdentity {
    protected EmployeeId() {
        super();
    }

    public EmployeeId(String id) {
        super(id);
    }

    public static EmployeeId random() {
        return new EmployeeId(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof EmployeeId other) {
            return other.getId().equals(id);
        }
        return false;
    }
}
