package com.cocroachden.planner.user;

import com.cocroachden.planner.user.repository.RegisteredUserRecord;
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
    private String username;
    @Nonnull
    private List<@Nonnull String> authorities;

    public static RegisteredUserDTO from(RegisteredUserRecord user) {
        return new RegisteredUserDTO(user.getUsername().getId(), user.getAuthorities());
    }
}
