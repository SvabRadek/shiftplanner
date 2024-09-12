package com.cocroachden.planner.user.repository;

import com.cocroachden.planner.user.RegisteredUserRecord;
import com.cocroachden.planner.user.RegisteredUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUserRecord, RegisteredUserId> {
}
