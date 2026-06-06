package com.example.MatchifyBackend.dto;

import com.example.MatchifyBackend.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PreferenceResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private Gender preferredGender;
    private Integer minAge;
    private Integer maxAge;
    private String city;
}