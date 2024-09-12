package com.cocroachden.planner.user.command.removeuser;

import com.cocroachden.planner.common.messaging.Command;

public record DeleteRegisteredUserCommand(
        String email
) implements Command {
}
