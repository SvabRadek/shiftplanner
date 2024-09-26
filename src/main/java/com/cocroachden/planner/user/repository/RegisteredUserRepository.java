package com.cocroachden.planner.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUserRecord, String> {
    Boolean existsByUsernameAndAuthoritiesContaining(String username, String authority);
}
