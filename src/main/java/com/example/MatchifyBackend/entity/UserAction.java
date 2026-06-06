package com.example.MatchifyBackend.entity;

import com.example.MatchifyBackend.enums.ActionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "user_actions")
public class UserAction extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private User targetUser;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private LocalDateTime actionTime;
}