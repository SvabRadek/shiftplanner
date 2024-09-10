package com.cocroachden.planner.user.service;

import com.cocroachden.planner.user.command.RegisterUserCommand;
import com.cocroachden.planner.user.command.UserHasBeenRegistered;
import com.cocroachden.planner.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisteredUserService {

    private final UserRepository userRepository;

    @EventListener
    public UserHasBeenRegistered handle(RegisterUserCommand command) {
        if (userRepository.existsById(command.user().getEmail())) {
            throw new IllegalArgumentException("User with email %s already exists!".formatted(command.user().getEmail()));
        }
        var savedUser = userRepository.save(command.user());
        return new UserHasBeenRegistered(savedUser);
    }
}
