package com.cocroachden.planner.user.command.registeruser;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.user.RegisteredUser;

public record UserHasBeenRegistered(RegisteredUser user) implements Event {
}
