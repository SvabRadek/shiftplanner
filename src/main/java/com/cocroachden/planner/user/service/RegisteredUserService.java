package com.cocroachden.planner.user.service;

import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.user.RegisteredUser;
import com.cocroachden.planner.user.command.addauthority.AddAuthorityCommand;
import com.cocroachden.planner.user.command.addauthority.AuthorityHasBeenAdded;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.command.registeruser.UserHasBeenRegistered;
import com.cocroachden.planner.user.repository.RegisteredUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class RegisteredUserService {

    private final RegisteredUserRepository registeredUserRepository;

    @EventListener
    public UserHasBeenRegistered handle(RegisterUserCommand command) {
        if (registeredUserRepository.existsById(command.email())) {
            throw new IllegalArgumentException("User with email %s already exists!".formatted(command.email()));
        }
        var savedUser = registeredUserRepository.save(new RegisteredUser(
                command.email(),
                command.hashedPassword(),
                Arrays.asList(command.authorities())
        ));
        return new UserHasBeenRegistered(savedUser);
    }

    @EventListener
    public AuthorityHasBeenAdded handle(AddAuthorityCommand command) {
        var user = registeredUserRepository.findById(command.email())
                .orElseThrow(() -> new IllegalArgumentException("User with email %s does not exists!".formatted(command.email())));
        var authorities = user.getAuthorities();
        authorities.addAll(Arrays.stream(command.authority()).map(Authorities::getRole).toList());
        user.setAuthorities(authorities.stream().distinct().toList());
        var updatedUser = registeredUserRepository.save(user);
        return new AuthorityHasBeenAdded(updatedUser.getEmail(), updatedUser.getAuthorities().stream().map(Authorities::fromRole).toList());
    }
}
