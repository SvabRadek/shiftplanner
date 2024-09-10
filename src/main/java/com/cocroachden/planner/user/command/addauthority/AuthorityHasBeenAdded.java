package com.cocroachden.planner.user.command.addauthority;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.security.Authorities;

public record AuthorityHasBeenAdded(
        String email,
        Authorities authority
) implements Event {
}
