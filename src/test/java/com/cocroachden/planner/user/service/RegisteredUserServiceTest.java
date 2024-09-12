package com.cocroachden.planner.user.service;

import com.cocroachden.AbstractIntegrationTest;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.command.registeruser.UserHasBeenRegistered;
import com.cocroachden.planner.user.repository.RegisteredUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegisteredUserServiceTest extends AbstractIntegrationTest {

    @Autowired
    private RegisteredUserRepository repository;

    @Test
    public void itCanRegisterNewUser() {
        var command = new RegisterUserCommand(
                "new.user@gmail.com",
                "23k4jh235k2jh5"
        );
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(UserHasBeenRegistered.class);
    }



}