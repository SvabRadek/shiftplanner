package com.cocroachden.planner.user.repository;


import com.cocroachden.planner.user.RegisteredUserId;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "registered_user")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class RegisteredUserRecord {
    @Id
    private String username;
    @Column(name = "password")
    private String hashedPassword;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "registered_user_username"))
    private List<String> authorities = new ArrayList<>();

    public RegisteredUserId getUsername() {
        return RegisteredUserId.from(username);
    }
}
