package com.cocroachden.planner.user;

import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RegisteredUserDTO {
    @Nonnull
    private String email;
    @Nonnull
    private List<@Nonnull String> authorities;

    public static RegisteredUserDTO from(RegisteredUserRecord user) {
        return new RegisteredUserDTO(user.getEmail().getId(), user.getAuthorities());
    }
}
