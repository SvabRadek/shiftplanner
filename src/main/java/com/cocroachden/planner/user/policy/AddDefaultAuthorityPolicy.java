package com.cocroachden.planner.user.policy;

import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.user.command.addauthority.AddAuthoritiesCommand;
import com.cocroachden.planner.user.command.registeruser.UserHasBeenRegistered;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AddDefaultAuthorityPolicy {
    @EventListener
    public AddAuthoritiesCommand on(UserHasBeenRegistered event) {
        //Add default role USER to newly registered user
        var email = event.user().getEmail();
        var roles = event.user().getAuthorities();
        if (roles.stream().noneMatch(r -> r.equalsIgnoreCase(Authorities.USER.getRole()))) {
            return new AddAuthoritiesCommand(email, Authorities.USER.getRole());
        }
        return null;
    }
}
