package com.example.MatchifyBackend.repository;

import com.example.MatchifyBackend.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
