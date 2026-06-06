package com.example.MatchifyBackend.entity;

import com.example.MatchifyBackend.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "preferences")
public class Preference extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @Enumerated(EnumType.STRING)
    private Gender preferredGender;
    private Integer minAge;
    private Integer maxAge;
    private String city;
}