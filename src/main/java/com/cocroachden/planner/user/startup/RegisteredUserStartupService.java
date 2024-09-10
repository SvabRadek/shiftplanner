package com.cocroachden.planner.user.startup;

import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.user.RegisteredUser;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.query.UserQuery;
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

    private final UserQuery userQuery;
    private final PasswordEncoder encoder;

    @EventListener
    public List<RegisterUserCommand> onApplicationEvent(ContextRefreshedEvent event) {
        return Stream
                .of(
                        new RegisteredUser("user@planning.com", encoder.encode("1234"), List.of(Authorities.USER.getRole())),
                        new RegisteredUser("admin@planning.com", encoder.encode("1234"), List.of(Authorities.USER.getRole(), Authorities.ADMIN.getRole()))
                ).filter(user -> !userQuery.userExists(user.getEmail()))
                .map(RegisterUserCommand::new)
                .toList();
    }
}
