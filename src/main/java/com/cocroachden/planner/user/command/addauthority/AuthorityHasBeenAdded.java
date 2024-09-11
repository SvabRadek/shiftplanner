package com.cocroachden.planner.user.command.addauthority;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.security.Authorities;

import java.util.List;

public record AuthorityHasBeenAdded(
        String email,
        List<Authorities> authority
) implements Event {
}
