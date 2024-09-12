package com.cocroachden.planner.user.command.removeuser;

import com.cocroachden.planner.common.messaging.Event;

public record RegisteredUserHasBeenDeleted(
        String email
) implements Event {
}
