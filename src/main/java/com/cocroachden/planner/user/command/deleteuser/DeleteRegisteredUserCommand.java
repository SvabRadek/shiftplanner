package com.cocroachden.planner.user.command.deleteuser;

import com.cocroachden.planner.common.messaging.Command;

public record DeleteRegisteredUserCommand(
        String email
) implements Command {
}
