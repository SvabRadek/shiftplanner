package com.cocroachden.planner.user.endpoint;

import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.query.UserQuery;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@Endpoint
@AllArgsConstructor
public class UserEndpoint {

    private final UserQuery userQuery;
    private final ApplicationEventPublisher publisher;

    @PermitAll
    public UserDTO findUser(String email, String hashedPassword) {
        return userQuery.findUser(email, hashedPassword)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login information!"));
    }

    @AnonymousAllowed
    public void registerUser(String email, String hashedPassword) {
        if (userQuery.userExists(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists!");
        }
        publisher.publishEvent(new RegisterUserCommand(email, hashedPassword));
    }
}
