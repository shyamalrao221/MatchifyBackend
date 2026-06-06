package com.example.MatchifyBackend.service;

import com.example.MatchifyBackend.dto.MatchResponseDto;
import com.example.MatchifyBackend.entity.Preference;
import com.example.MatchifyBackend.entity.UserProfile;
import com.example.MatchifyBackend.repository.PreferenceRepository;
import com.example.MatchifyBackend.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    public List<MatchResponseDto> getMatches(Long userId) {
        List<MatchResponseDto> matches = new ArrayList<>();

        UserProfile currentUserProfile = userProfileRepository.findByUserId(userId).orElse(null);
        Preference currentUserPreference = preferenceRepository.findByUserId(userId).orElse(null);

        if (currentUserProfile == null || currentUserPreference == null) {
            return matches;
        }

        List<UserProfile> allProfiles = userProfileRepository.findAll();

        for (UserProfile otherProfile : allProfiles) {
            Long otherUserId = otherProfile.getUser().getId();

            if (otherUserId.equals(userId)) {
                continue;
            }

            Preference otherUserPreference = preferenceRepository.findByUserId(otherUserId).orElse(null);

            if (otherUserPreference == null) {
                continue;
            }

            boolean currentLikesOther = isPreferenceMatched(currentUserPreference, otherProfile);
            boolean otherLikesCurrent = isPreferenceMatched(otherUserPreference, currentUserProfile);

            if (currentLikesOther && otherLikesCurrent) {
                MatchResponseDto dto = new MatchResponseDto(
                        otherProfile.getUser().getId(),
                        otherProfile.getUser().getFirstName(),
                        otherProfile.getUser().getLastName(),
                        otherProfile.getAge(),
                        otherProfile.getGender() != null ? otherProfile.getGender().name() : null,
                        otherProfile.getCity(),
                        otherProfile.getBio()
                );

                matches.add(dto);
            }
        }

        return matches;
    }

    private boolean isPreferenceMatched(Preference preference, UserProfile profile) {
        if (preference.getPreferredGender() != null && profile.getGender() != null) {
            if (!preference.getPreferredGender().equals(profile.getGender())) {
                return false;
            }
        }

        if (preference.getMinAge() != null) {
            if (profile.getAge() < preference.getMinAge()) {
                return false;
            }
        }

        if (preference.getMaxAge() != null) {
            if (profile.getAge() > preference.getMaxAge()) {
                return false;
            }
        }

        if (preference.getCity() != null && !preference.getCity().isBlank()) {
            if (profile.getCity() == null || !preference.getCity().equalsIgnoreCase(profile.getCity())) {
                return false;
            }
        }

        return true;
    }
}