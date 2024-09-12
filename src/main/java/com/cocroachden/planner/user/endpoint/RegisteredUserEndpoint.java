package com.cocroachden.planner.user.endpoint;

import com.cocroachden.planner.user.RegisteredUserDTO;
import com.cocroachden.planner.user.RegisteredUserId;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.query.RegisteredUserQuery;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@Endpoint
@AllArgsConstructor
public class RegisteredUserEndpoint {

    private final RegisteredUserQuery registeredUserQuery;
    private final ApplicationEventPublisher publisher;

    @PermitAll
    public RegisteredUserDTO findUser(String email, String hashedPassword) {
        return registeredUserQuery.findUser(RegisteredUserId.from(email), hashedPassword)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login information!"));
    }

    @AnonymousAllowed
    public void registerUser(String email, String hashedPassword) {
        var userId = RegisteredUserId.from(email);
        if (registeredUserQuery.userExists(userId)) {
            throw new IllegalArgumentException("User with registeredUserId " + email + " already exists!");
        }
        publisher.publishEvent(new RegisterUserCommand(userId, hashedPassword));
    }
}
