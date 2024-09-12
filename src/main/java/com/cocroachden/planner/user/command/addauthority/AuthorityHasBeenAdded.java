package com.cocroachden.planner.user.command.addauthority;

import com.cocroachden.planner.common.messaging.Event;

import java.util.List;

public record AuthorityHasBeenAdded(
        String email,
        List<String> authority
) implements Event {
}
