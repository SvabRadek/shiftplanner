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
    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "registered_user_email"))
    @Column(name = "email")
    private RegisteredUserId email;
    @Column(name = "password")
    private String hashedPassword;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "registered_user_email"))
    private List<String> authorities = new ArrayList<>();
}
