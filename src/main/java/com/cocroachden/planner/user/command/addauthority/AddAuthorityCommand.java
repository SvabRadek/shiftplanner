package com.cocroachden.planner.user.command.addauthority;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.security.Authorities;

public record AddAuthorityCommand(
        String email,
        Authorities authority
) implements Command {
}
