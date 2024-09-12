package com.cocroachden.planner.user;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RegisteredUserId implements Serializable {

    private String id;

    public static RegisteredUserId from(String id) {
        return new RegisteredUserId(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RegisteredUserId other) {
            return id.equals(other.id);
        }
        return false;
    }
}
