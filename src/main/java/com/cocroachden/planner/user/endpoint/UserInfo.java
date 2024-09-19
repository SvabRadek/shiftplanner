package com.cocroachden.planner.user.endpoint;

import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserInfo {
    private @Nonnull String username;
    private @Nonnull List<@Nonnull String> authorities;
}
