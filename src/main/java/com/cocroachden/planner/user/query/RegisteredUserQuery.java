package com.cocroachden.planner.user.query;

import com.cocroachden.planner.user.RegisteredUserDTO;
import com.cocroachden.planner.user.RegisteredUserId;
import com.cocroachden.planner.user.repository.RegisteredUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisteredUserQuery implements UserDetailsService {

    private final RegisteredUserRepository registeredUserRepository;

    public Optional<RegisteredUserDTO> findUser(RegisteredUserId userId, String hashedPassword) {
        var user = registeredUserRepository.findById(userId);
        if (user.isPresent() && user.get().getHashedPassword().equals(hashedPassword)) {
            return user.map(RegisteredUserDTO::from);
        }
        return Optional.empty();
    }

    public Boolean userExists(RegisteredUserId userId) {
        return registeredUserRepository.existsById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return registeredUserRepository.findById(new RegisteredUserId(username))
                .map(user -> new User(
                        user.getEmail().getId(),
                        user.getHashedPassword(),
                        true,
                        true,
                        true,
                        true,
                        user.getAuthorities().stream().map(a -> (GrantedAuthority) () -> a).toList()
                )).orElseThrow(() -> new UsernameNotFoundException("Username %s was not found!".formatted(username)));
    }
}
