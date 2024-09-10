package com.cocroachden.planner.user;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "registered_user")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisteredUser {
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String hashedPassword;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "registered_user_email"))
    private List<String> authorities = new ArrayList<>();
}
