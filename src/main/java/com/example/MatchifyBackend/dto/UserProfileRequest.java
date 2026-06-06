package com.example.MatchifyBackend.dto;

import com.example.MatchifyBackend.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileRequest {
    private int age;
    private Gender gender;
    private String city;
    private String bio;
    private Long height;

}
