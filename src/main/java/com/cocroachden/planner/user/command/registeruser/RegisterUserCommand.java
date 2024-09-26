package com.cocroachden.planner.user.command.registeruser;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.user.RegisteredUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class RegisterUserCommand implements Command {
    private final RegisteredUserId username;
    private final String hashedPassword;
    private final List<String> authorities;

    public RegisterUserCommand(RegisteredUserId username, String hashedPassword, String... authorities) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.authorities = Arrays.stream(authorities).toList();
    }

    public RegisterUserCommand(String username, String hashedPassword, String... authorities) {
        this.username = RegisteredUserId.from(username);
        this.hashedPassword = hashedPassword;
        this.authorities = Arrays.stream(authorities).toList();
    }
}
