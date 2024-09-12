package com.cocroachden.planner.user.endpoint;

import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.query.RegisteredUserQuery;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@Endpoint
@AllArgsConstructor
public class UserEndpoint {

    private final RegisteredUserQuery registeredUserQuery;
    private final ApplicationEventPublisher publisher;

    @PermitAll
    public UserDTO findUser(String email, String hashedPassword) {
        return registeredUserQuery.findUser(email, hashedPassword)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login information!"));
    }

    @AnonymousAllowed
    public void registerUser(String email, String hashedPassword) {
        if (registeredUserQuery.userExists(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists!");
        }
        publisher.publishEvent(new RegisterUserCommand(email, hashedPassword));
    }
}
