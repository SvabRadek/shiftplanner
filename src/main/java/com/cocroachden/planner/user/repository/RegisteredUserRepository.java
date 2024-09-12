package com.cocroachden.planner.user.repository;

import com.cocroachden.planner.user.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, String> {
}
