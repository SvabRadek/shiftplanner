package com.cocroachden.planner.employee;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeId implements Serializable {
    private String id;
    public static EmployeeId random() {
        return new EmployeeId(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EmployeeId other) {
            return other.getId().equals(id);
        }
        return false;
    }
}
