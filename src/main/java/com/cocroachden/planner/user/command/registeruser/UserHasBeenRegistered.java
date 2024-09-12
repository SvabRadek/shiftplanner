package com.cocroachden.planner.user.command.registeruser;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.user.RegisteredUserRecord;

public record UserHasBeenRegistered(RegisteredUserRecord user) implements Event {
}
