package com.example.MatchifyBackend.service;

import com.example.MatchifyBackend.entity.User;
import com.example.MatchifyBackend.repository.PreferenceRepository;
import com.example.MatchifyBackend.repository.UserProfileRepository;
import com.example.MatchifyBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Transactional
    public String deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userProfileRepository.deleteByUserId(userId);
        preferenceRepository.deleteByUserId(userId);
        userRepository.delete(user);

        return "User deleted successfully with userId: " + userId;
    }
}