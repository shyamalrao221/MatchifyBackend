package com.example.MatchifyBackend.dto;

import com.example.MatchifyBackend.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PreferenceRequest {
    private Gender preferredGender;
    private Integer minAge;
    private Integer maxAge;
    private String city;
}