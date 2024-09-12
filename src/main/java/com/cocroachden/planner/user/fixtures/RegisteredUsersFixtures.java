package com.cocroachden.planner.user.fixtures;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.fixtures.SpecificFixtureGenerator;
import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.user.RegisteredUserId;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RegisteredUsersFixtures implements SpecificFixtureGenerator {

    private final PasswordEncoder encoder;

    @Override
    public List<Command> generateCommands() {
        return List.of(
                new RegisterUserCommand(
                        RegisteredUserId.from("user@planning.com"),
                        encoder.encode("1234")
                ),
                new RegisterUserCommand(
                        RegisteredUserId.from("admin@planning.com"),
                        encoder.encode("1234"),
                        Authorities.ADMIN.getRole()
                )
        );
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
