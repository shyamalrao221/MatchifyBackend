package com.example.MatchifyBackend.repository;

import com.example.MatchifyBackend.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    List<UserProfile> findAll();
}
