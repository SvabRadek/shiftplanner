package com.cocroachden.planner.user.command.deleteuser;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.user.RegisteredUserId;

public record DeleteRegisteredUserCommand(
        RegisteredUserId registeredUserId
) implements Command {
}
