package com.kuwarte.api.preferences.dto;

import com.kuwarte.api.preferences.UserPreferences;
import com.kuwarte.api.preferences.enums.ChatProvider;
import com.kuwarte.api.preferences.enums.Theme;

import java.time.Instant;

public record UserPreferencesResponse(
        Theme theme,
        ChatProvider chatProvider,
        String chatModel,
        String embeddingModel,
        int chunkSize,
        Instant updatedAt) {

    public static UserPreferencesResponse toResponse(UserPreferences preferences) {
        return new UserPreferencesResponse(
                preferences.getTheme(),
                preferences.getChatProvider(),
                preferences.getChatModel(),
                preferences.getEmbeddingModel(),
                preferences.getChunkSize(),
                preferences.getUpdatedAt());
    }
}
