package com.kuwarte.api.preferences;

import com.kuwarte.api.auth.User;
import com.kuwarte.api.preferences.dto.UpdateUserPreferencesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPreferencesService {

    private final UserPreferencesRepository preferencesRepository;

    @Transactional
    public UserPreferences createDefaultsFor(User user) {
        UserPreferences preferences = UserPreferences.builder()
                .user(user)
                .build();
        return preferencesRepository.save(preferences);
    }

    @Transactional(readOnly = true)
    public UserPreferences getForUser(User user) {
        return preferencesRepository.findByUserId(user.getId())
                .orElseGet(() -> createDefaultsFor(user));
    }

    @Transactional
    public UserPreferences update(User user, UpdateUserPreferencesRequest request) {
        UserPreferences preferences = getForUser(user);

        if (request.theme() != null) {
            preferences.setTheme(request.theme());
        }
        if (request.chatProvider() != null) {
            preferences.setChatProvider(request.chatProvider());
        }
        if (request.chatModel() != null && !request.chatModel().isBlank()) {
            preferences.setChatModel(request.chatModel());
        }
        if (request.chunkSize() != null) {
            preferences.setChunkSize(request.chunkSize());
        }
        return preferencesRepository.save(preferences);
    }
}
