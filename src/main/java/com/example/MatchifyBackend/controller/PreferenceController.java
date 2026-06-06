package com.example.MatchifyBackend.controller;

import com.example.MatchifyBackend.dto.PreferenceRequest;
import com.example.MatchifyBackend.dto.PreferenceResponse;
import com.example.MatchifyBackend.service.AuthHelperService;
import com.example.MatchifyBackend.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private AuthHelperService authHelperService;

    @PostMapping("/me")
    public PreferenceResponse saveMyPreference(@RequestBody PreferenceRequest request) {
        Long currentUserId = authHelperService.getCurrentUser().getId();
        return preferenceService.savePreference(request, currentUserId);
    }

    @GetMapping("/me")
    public PreferenceResponse getMyPreference() {
        Long currentUserId = authHelperService.getCurrentUser().getId();
        return preferenceService.getPreference(currentUserId);
    }

    @DeleteMapping("/me")
    public String deleteMyPreference() {
        Long currentUserId = authHelperService.getCurrentUser().getId();
        return preferenceService.deletePreference(currentUserId);
    }
}