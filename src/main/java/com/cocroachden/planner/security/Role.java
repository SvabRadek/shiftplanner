package com.cocroachden.planner.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class Role implements GrantedAuthority {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    private final String role;

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Role other) {
            return role.equals(other.role);
        }
        if (obj instanceof String string) {
            return role.equals(string);
        }
        return false;
    }
}
