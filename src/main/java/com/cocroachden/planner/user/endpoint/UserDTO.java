package com.cocroachden.planner.user.endpoint;

import com.cocroachden.planner.user.RegisteredUser;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDTO {
    @Nonnull
    private String email;
    @Nonnull
    private List<@Nonnull String> authorities;

    public static UserDTO from(RegisteredUser user) {
        return new UserDTO(user.getEmail(), user.getAuthorities());
    }
}
