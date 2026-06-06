package com.example.MatchifyBackend.controller;

import com.example.MatchifyBackend.dto.UserProfileRequest;
import com.example.MatchifyBackend.dto.UserProfileResponse;
import com.example.MatchifyBackend.service.AuthHelperService;
import com.example.MatchifyBackend.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private AuthHelperService authHelperService;

    @PostMapping("/me")
    public UserProfileResponse createOrUpdateMyProfile(@RequestBody UserProfileRequest request) {
        Long currentUserId = authHelperService.getCurrentUser().getId();
        return userProfileService.createOrUpdateProfile(request, currentUserId);
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile() {
        Long currentUserId = authHelperService.getCurrentUser().getId();
        return userProfileService.getProfile(currentUserId);
    }

    @DeleteMapping("/me")
    public String deleteMyProfile() {
        Long currentUserId = authHelperService.getCurrentUser().getId();
        return userProfileService.deleteProfile(currentUserId);
    }

    @GetMapping("/all")
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileService.getAllProfiles();
    }
}