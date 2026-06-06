package com.example.MatchifyBackend.service;

import com.example.MatchifyBackend.dto.UserProfileRequest;
import com.example.MatchifyBackend.dto.UserProfileResponse;
import com.example.MatchifyBackend.entity.User;
import com.example.MatchifyBackend.entity.UserProfile;
import com.example.MatchifyBackend.repository.UserProfileRepository;
import com.example.MatchifyBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfileResponse createOrUpdateProfile(UserProfileRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElse(new UserProfile());

        userProfile.setUser(user);
        userProfile.setAge(request.getAge());
        userProfile.setGender(request.getGender());
        userProfile.setCity(request.getCity());
        userProfile.setBio(request.getBio());
        userProfile.setHeight(request.getHeight());

        UserProfile savedProfile = userProfileRepository.save(userProfile);
        return mapToResponse(savedProfile);
    }

    public UserProfileResponse getProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToResponse(profile);
    }

    public String deleteProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        userProfileRepository.delete(profile);
        return "Profile deleted successfully for user: " + userId;
    }

    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> profiles = userProfileRepository.findAll();

        return profiles.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private UserProfileResponse mapToResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getUser().getFirstName(),
                profile.getUser().getLastName(),
                profile.getAge(),
                profile.getGender(),
                profile.getCity(),
                profile.getBio(),
                profile.getHeight()
        );
    }
}