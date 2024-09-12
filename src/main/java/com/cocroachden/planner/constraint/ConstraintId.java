package com.cocroachden.planner.constraint;

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
public class ConstraintId implements Serializable {

    private String id;

    public static ConstraintId random() {
        return new ConstraintId(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConstraintId other) {
            return other.id.equals(id);
        }
        return false;
    }
}
