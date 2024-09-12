package com.cocroachden.planner.user.command.deleteuser;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.user.RegisteredUserId;

public record RegisteredUserHasBeenDeleted(
        RegisteredUserId registeredUserId
) implements Event {
}
