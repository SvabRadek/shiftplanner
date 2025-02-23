package com.cocroachden.planner.user.service;

import com.cocroachden.planner.AbstractMessagingTest;
import com.cocroachden.planner.security.Authorities;
import com.cocroachden.planner.user.RegisteredUserId;
import com.cocroachden.planner.user.command.addauthority.AddAuthoritiesCommand;
import com.cocroachden.planner.user.command.addauthority.AuthorityHasBeenAdded;
import com.cocroachden.planner.user.command.registeruser.RegisterUserCommand;
import com.cocroachden.planner.user.command.registeruser.UserHasBeenRegistered;
import com.cocroachden.planner.user.command.deleteuser.DeleteRegisteredUserCommand;
import com.cocroachden.planner.user.command.deleteuser.RegisteredUserHasBeenDeleted;
import com.cocroachden.planner.user.query.RegisteredUserQuery;
import com.cocroachden.planner.user.repository.RegisteredUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegisteredUserServiceTest extends AbstractMessagingTest {

    @Autowired
    private RegisteredUserQuery registeredUserQuery;

    @Autowired
    private RegisteredUserRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    public void itCanRegisterNewUserWithDefaultAuthority() {
        var userId = RegisteredUserId.from("irrelevant");
        var command = new RegisterUserCommand(
                userId,
                "irrelevant"
        );
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(UserHasBeenRegistered.class);
        this.thenExactlyOneEventHasBeenDispatched(AuthorityHasBeenAdded.class);
        Assertions.assertThat(repository.existsById(userId.getId())).isTrue();
        var user = repository.findById(userId.getId()).orElseThrow();
        Assertions.assertThat(user.getAuthorities()).hasSize(1);
    }

    @Test
    public void itCanRegisterNewUserWithAuthority() {
        var userId = RegisteredUserId.from("irrelevant");
        var command = new RegisterUserCommand(
                userId,
                "irrelevant",
                Authorities.USER.getRole()
        );
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(UserHasBeenRegistered.class);
        this.thenNoEventsOfTypeHaveBeenDispatched(AuthorityHasBeenAdded.class);
        Assertions.assertThat(repository.existsById(userId.getId())).isTrue();
    }

    @Test
    public void itThrowsWhenTryingToCreateExistingUser() {
        var email = "irrelevant";
        var command = new RegisterUserCommand(
                email,
                "irrelevant"
        );
        this.givenCommandHasBeenSent(command);
        this.thenCommandThrowsException(command, IllegalArgumentException.class);
    }

    @Test
    public void itCanDeleteUser() {
        var userId = RegisteredUserId.from("irrelevant");
        var command = new RegisterUserCommand(
                userId,
                "irrelevant"
        );
        this.givenCommandHasBeenSent(command);
        var testedCommand = new DeleteRegisteredUserCommand(userId);
        this.whenCommandHasBeenSent(testedCommand);
        this.thenExactlyOneEventHasBeenDispatched(RegisteredUserHasBeenDeleted.class);
        Assertions.assertThat(this.repository.existsById(userId.getId())).isFalse();
    }

    @Test
    public void itIgnoresWhenDeletingNonExistingUser() {
        var userId = RegisteredUserId.from("irrelevant");
        var command = new DeleteRegisteredUserCommand(userId);
        this.whenCommandHasBeenSent(command);
        this.thenNoEventsOfTypeHaveBeenDispatched(RegisteredUserHasBeenDeleted.class);
    }

    @Test
    public void itCanAddAuthoritiesToExistingUser() {
        var userId = RegisteredUserId.from("irrelevant");
        var command = new RegisterUserCommand(
                userId,
                "irrelevant"
        );
        this.givenCommandHasBeenSent(command);
        var testedCommand = new AddAuthoritiesCommand(
                userId,
                Authorities.ADMIN.getRole()
        );
        this.whenCommandHasBeenSent(testedCommand);
        this.thenExactlyOneEventHasBeenDispatched(AuthorityHasBeenAdded.class);
        var authorities = registeredUserQuery.loadUserByUsername(userId.getId()).getAuthorities();
        Assertions.assertThat(authorities).hasSize(2);
    }
}