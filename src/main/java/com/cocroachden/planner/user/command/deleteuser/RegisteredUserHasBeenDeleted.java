package com.cocroachden.planner.user.command.deleteuser;

import com.cocroachden.planner.common.messaging.Event;

public record RegisteredUserHasBeenDeleted(
        String email
) implements Event {
}
