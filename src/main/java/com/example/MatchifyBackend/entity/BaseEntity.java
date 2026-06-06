package com.example.MatchifyBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
