package com.cocroachden.planner.user.command.addauthority;

import com.cocroachden.planner.common.messaging.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AddAuthoritiesCommand implements Command {
    private final String email;
    private final List<String> authorities;

    public AddAuthoritiesCommand(
            String email,
            String... authorities
    ) {
        this.email = email;
        this.authorities = List.of(authorities);
    }
}
