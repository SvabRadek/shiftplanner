package com.cocroachden.planner.employee;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeId implements Serializable {
    private String id;
    public static EmployeeId random() {
        return new EmployeeId(UUID.randomUUID().toString());
    }
    public static EmployeeId from(String id) {
        return new EmployeeId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EmployeeId other) {
            return other.getId().equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
