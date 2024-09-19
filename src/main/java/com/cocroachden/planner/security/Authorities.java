package com.cocroachden.planner.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Authorities {
    USER(Role.ROLE_USER),
    ADMIN(Role.ROLE_ADMIN);
    private final String role;

    public static Authorities fromRole(String role) {
        return Arrays.stream(Authorities.values())
                .filter(a -> a.getRole().equalsIgnoreCase(role))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + role));
    }

}
