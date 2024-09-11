package com.cocroachden.planner.user.service;

import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.user.RegisteredUser;
import com.cocroachden.planner.user.command.addauthority.AddAuthorityCommand;
import com.cocroachden.planner.user.command.addauthority.AuthorityHasBeenAdded;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.command.registeruser.UserHasBeenRegistered;
import com.cocroachden.planner.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class RegisteredUserService {

    private final UserRepository userRepository;

    @EventListener
    public UserHasBeenRegistered handle(RegisterUserCommand command) {
        if (userRepository.existsById(command.email())) {
            throw new IllegalArgumentException("User with email %s already exists!".formatted(command.email()));
        }
        var savedUser = userRepository.save(new RegisteredUser(
                command.email(),
                command.hashedPassword(),
                new ArrayList<>()
        ));
        return new UserHasBeenRegistered(savedUser);
    }

    @EventListener
    public AuthorityHasBeenAdded handle(AddAuthorityCommand command) {
        var user = userRepository.findById(command.email())
                .orElseThrow(() -> new IllegalArgumentException("User with email %s does not exists!".formatted(command.email())));
        var authorities = user.getAuthorities();
        authorities.addAll(Arrays.stream(command.authority()).map(Authorities::getRole).toList());
        user.setAuthorities(authorities.stream().distinct().toList());
        var updatedUser = userRepository.save(user);
        return new AuthorityHasBeenAdded(updatedUser.getEmail(), updatedUser.getAuthorities().stream().map(Authorities::fromRole).toList());
    }
}
