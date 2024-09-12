package com.cocroachden.planner.user.command.registeruser;

import com.cocroachden.planner.common.messaging.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public class RegisterUserCommand implements Command {
    private final String email;
    private final String hashedPassword;
    private final List<String> authorities;

    public RegisterUserCommand(String email, String hashedPassword, String... authorities) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.authorities = Arrays.stream(authorities).toList();
    }
}
