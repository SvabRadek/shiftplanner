package com.cocroachden.planner.user.query;

import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.user.RegisteredUserDTO;
import com.cocroachden.planner.user.RegisteredUserId;
import com.cocroachden.planner.security.Role;
import com.cocroachden.planner.user.repository.RegisteredUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RegisteredUserQuery implements UserDetailsService {

    private final RegisteredUserRepository registeredUserRepository;

    public Optional<RegisteredUserDTO> findUser(RegisteredUserId userId, String hashedPassword) {
        var user = registeredUserRepository.findById(userId.getId());
        if (user.isPresent() && user.get().getHashedPassword().equals(hashedPassword)) {
            return user.map(RegisteredUserDTO::from);
        }
        return Optional.empty();
    }

    public Boolean userExists(RegisteredUserId userId) {
        return registeredUserRepository.existsById(userId.getId());
    }

    public Boolean hasRole(String username, Authorities authority) {
        return registeredUserRepository.existsByUsernameAndAuthoritiesContaining(username, authority.getRole());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return registeredUserRepository.findById(username)
                .map(user -> new User(
                        user.getUsername().getId(),
                        user.getHashedPassword(),
                        true,
                        true,
                        true,
                        true,
                        user.getAuthorities().stream().map(Role::new).toList()
                )).orElseThrow(() -> new UsernameNotFoundException("Username %s was not found!".formatted(username)));
    }
}
