package com.example.MatchifyBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String token;
    private String message;
}