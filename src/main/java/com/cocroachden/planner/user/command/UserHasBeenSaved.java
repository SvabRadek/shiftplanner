package com.cocroachden.planner.user.command;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.user.RegisteredUser;

public record UserHasBeenSaved(RegisteredUser user) implements Event {
}
