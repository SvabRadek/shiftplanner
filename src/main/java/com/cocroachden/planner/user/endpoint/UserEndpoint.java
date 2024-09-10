package com.cocroachden.planner.user.endpoint;

import com.cocroachden.planner.user.query.UserQuery;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import lombok.AllArgsConstructor;

@Endpoint
@AllArgsConstructor
public class UserEndpoint {

    private final UserQuery userQuery;

    @AnonymousAllowed
    public UserDTO findUser(String email, String hashedPassword) {
        return userQuery.findUser(email, hashedPassword)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login information!"));
    }
}
