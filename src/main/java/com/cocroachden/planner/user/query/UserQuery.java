package com.cocroachden.planner.user.query;

import com.cocroachden.planner.user.endpoint.UserDTO;
import com.cocroachden.planner.user.repository.UserRepository;
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
public class UserQuery implements UserDetailsService {

    private final UserRepository userRepository;

    public Optional<UserDTO> findUser(String email, String hashedPassword) {
        var user = userRepository.findById(email);
        if (user.isPresent() && user.get().getHashedPassword().equals(hashedPassword)) {
            return user.map(UserDTO::from);
        }
        return Optional.empty();
    }

    public Boolean userExists(String email) {
        return userRepository.existsById(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
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
