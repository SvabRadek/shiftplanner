package com.cocroachden.planner.user.command.addauthority;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.user.RegisteredUserId;

import java.util.List;

public record AuthorityHasBeenAdded(
        RegisteredUserId registeredUserId,
        List<String> authority
) implements Event {
}
