package com.cocroachden.planner.user.command.addauthority;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.user.RegisteredUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AddAuthoritiesCommand implements Command {
    private final RegisteredUserId userId;
    private final List<String> authorities;

    public AddAuthoritiesCommand(
            RegisteredUserId userId,
            String... authorities
    ) {
        this.userId = userId;
        this.authorities = List.of(authorities);
    }
}
