package com.cocroachden.planner.user.command.registeruser;

import com.cocroachden.planner.common.messaging.Command;

public record RegisterUserCommand(
        String email,
        String hashedPassword,
        String... authorities
) implements Command {

}
