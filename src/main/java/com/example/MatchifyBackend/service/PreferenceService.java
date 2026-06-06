package com.example.MatchifyBackend.service;

import com.example.MatchifyBackend.dto.PreferenceRequest;
import com.example.MatchifyBackend.dto.PreferenceResponse;
import com.example.MatchifyBackend.entity.Preference;
import com.example.MatchifyBackend.entity.User;
import com.example.MatchifyBackend.repository.PreferenceRepository;
import com.example.MatchifyBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreferenceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    public PreferenceResponse savePreference(PreferenceRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Preference preference = preferenceRepository.findByUserId(userId)
                .orElse(new Preference());

        preference.setUser(user);
        preference.setPreferredGender(request.getPreferredGender());
        preference.setMinAge(request.getMinAge());
        preference.setMaxAge(request.getMaxAge());
        preference.setCity(request.getCity());

        Preference savedPreference = preferenceRepository.save(preference);

        return mapToResponse(savedPreference);
    }

    public PreferenceResponse getPreference(Long userId) {
        Preference preference = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preference not found"));

        return mapToResponse(preference);
    }

    public String deletePreference(Long userId) {
        Preference preference = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preference not found"));

        preferenceRepository.delete(preference);
        return "Preference deleted successfully for user: " + userId;
    }

    private PreferenceResponse mapToResponse(Preference preference) {
        return new PreferenceResponse(
                preference.getId(),
                preference.getUser().getId(),
                preference.getUser().getFirstName(),
                preference.getUser().getLastName(),
                preference.getPreferredGender(),
                preference.getMinAge(),
                preference.getMaxAge(),
                preference.getCity()
        );
    }
}