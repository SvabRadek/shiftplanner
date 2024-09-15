package com.cocroachden.planner.constraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConstraintId implements Serializable {

    private String id;

    public static ConstraintId random() {
        return new ConstraintId(UUID.randomUUID().toString());
    }

    public static ConstraintId from(String id) {
        return new ConstraintId(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConstraintId other) {
            return other.id.equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
