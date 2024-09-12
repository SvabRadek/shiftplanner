package com.cocroachden.planner.user.fixtures;

import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.user.RegisteredUser;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.query.RegisteredUserQuery;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class RegisteredUserStartupService {

    private final RegisteredUserQuery registeredUserQuery;
    private final PasswordEncoder encoder;

    @EventListener
    public List<RegisterUserCommand> onApplicationEvent(ContextRefreshedEvent event) {
        return Stream.of(
                        new RegisterUserCommand("user@planning.com", encoder.encode("1234")),
                        new RegisterUserCommand(
                                "admin@planning.com",
                                encoder.encode("1234"),
                                Authorities.ADMIN.getRole()
                        )
                ).filter(user -> !registeredUserQuery.userExists(user.getEmail()))
                .toList();
    }
}
