package com.example.MatchifyBackend.entity;


import com.example.MatchifyBackend.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_profiles")

public class UserProfile extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String city;
    private String bio;
    private Long height;




}
