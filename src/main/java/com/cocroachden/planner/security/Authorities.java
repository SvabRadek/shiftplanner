package com.cocroachden.planner.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Authorities {
    USER("user"),
    ADMIN("admin");
    private final String role;
}
