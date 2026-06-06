package com.example.MatchifyBackend.service;

import com.example.MatchifyBackend.dto.AuthResponse;
import com.example.MatchifyBackend.dto.LoginRequest;
import com.example.MatchifyBackend.dto.RegisterRequest;
import com.example.MatchifyBackend.entity.User;
import com.example.MatchifyBackend.enums.Role;
import com.example.MatchifyBackend.repository.UserRepository;
import com.example.MatchifyBackend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Email already in use"
            );
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                null,
                "User registered successfully"
        );
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmail(request.getEmail()).orElse(null);

                if (user != null) {
                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .authorities("ROLE_" + user.getRole().name())
                            .build();

                    String token = jwtService.generateToken(userDetails, user.getId());

                    return new AuthResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getRole().name(),
                            token,
                            "Login successful"
                    );
                }
            }

            return new AuthResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Invalid email or password"
            );

        } catch (AuthenticationException e) {
            return new AuthResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Invalid email or password"
            );
        }
    }
}