package com.example.MatchifyBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponseDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String city;
    private String bio;
}