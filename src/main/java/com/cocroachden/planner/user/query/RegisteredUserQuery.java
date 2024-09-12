package com.cocroachden.planner.user.query;

import com.cocroachden.planner.user.endpoint.UserDTO;
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

    public Optional<UserDTO> findUser(String email, String hashedPassword) {
        var user = registeredUserRepository.findById(email);
        if (user.isPresent() && user.get().getHashedPassword().equals(hashedPassword)) {
            return user.map(UserDTO::from);
        }
        return Optional.empty();
    }

    public Boolean userExists(String email) {
        return registeredUserRepository.existsById(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return registeredUserRepository.findById(username)
                .map(user -> new User(
                        user.getEmail(),
                        user.getHashedPassword(),
                        true,
                        true,
                        true,
                        true,
                        user.getAuthorities().stream().map(a -> (GrantedAuthority) () -> a).toList()
                )).orElseThrow(() -> new UsernameNotFoundException("Username %s was not found!".formatted(username)));
    }
}
