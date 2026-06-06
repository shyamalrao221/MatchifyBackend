package com.example.MatchifyBackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "matches")
public class Match extends BaseEntity {

    @ManyToOne
    private User user1;
    @ManyToOne
    private User user2;
    private LocalDateTime matchedAt;

}
