package com.example.MatchifyBackend.repository;

import com.example.MatchifyBackend.entity.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {
    // Custom query methods can be defined here if needed
}