package com.cocroachden.planner.user.command;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.user.RegisteredUser;

public record SaveUserCommand(RegisteredUser user) implements Command {
}
