package com.cocroachden.planner.user.service;

import com.cocroachden.planner.user.RegisteredUser;
import com.cocroachden.planner.user.command.addauthority.AddAuthoritiesCommand;
import com.cocroachden.planner.user.command.addauthority.AuthorityHasBeenAdded;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.command.registeruser.UserHasBeenRegistered;
import com.cocroachden.planner.user.command.deleteuser.DeleteRegisteredUserCommand;
import com.cocroachden.planner.user.command.deleteuser.RegisteredUserHasBeenDeleted;
import com.cocroachden.planner.user.repository.RegisteredUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RegisteredUserService {

    private final RegisteredUserRepository registeredUserRepository;

    @EventListener
    public UserHasBeenRegistered handle(RegisterUserCommand command) {
        log.debug("Handling RegisterUserCommand");
        if (registeredUserRepository.existsById(command.getEmail())) {
            throw new IllegalArgumentException("User with email %s already exists!".formatted(command.getEmail()));
        }
        var savedUser = registeredUserRepository.save(
                new RegisteredUser(
                        command.getEmail(),
                        command.getHashedPassword(),
                        command.getAuthorities().stream().map(String::toUpperCase).toList()
                )
        );
        return new UserHasBeenRegistered(savedUser);
    }

    @EventListener
    public AuthorityHasBeenAdded handle(AddAuthoritiesCommand command) {
        log.debug("Handling AddAuthorityCommand");
        var user = registeredUserRepository.findById(command.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User with email %s does not exists!".formatted(command.getEmail())));
        var authorities = user.getAuthorities();
        authorities.addAll(command.getAuthorities());
        user.setAuthorities(authorities.stream().map(String::toUpperCase).distinct().toList());
        var updatedUser = registeredUserRepository.save(user);
        return new AuthorityHasBeenAdded(updatedUser.getEmail(), command.getAuthorities());
    }

    @EventListener
    public RegisteredUserHasBeenDeleted handle(DeleteRegisteredUserCommand command) {
        log.debug("Handling DeleteRegisteredUserCommand");
        if (!registeredUserRepository.existsById(command.email())) {
            return null;
        }
        registeredUserRepository.deleteById(command.email());
        return new RegisteredUserHasBeenDeleted(command.email());
    }
}
