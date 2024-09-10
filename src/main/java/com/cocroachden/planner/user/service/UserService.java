package com.cocroachden.planner.user.service;

import com.cocroachden.planner.user.command.SaveUserCommand;
import com.cocroachden.planner.user.command.UserHasBeenSaved;
import com.cocroachden.planner.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @EventListener
    public UserHasBeenSaved handle(SaveUserCommand command) {
        if (userRepository.existsById(command.user().getEmail())) {
            throw new IllegalArgumentException("User with email %s already exists!".formatted(command.user().getEmail()));
        }
        var savedUser = userRepository.save(command.user());
        return new UserHasBeenSaved(savedUser);
    }
}
