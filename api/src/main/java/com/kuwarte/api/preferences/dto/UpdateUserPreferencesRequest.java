package com.kuwarte.api.preferences.dto;

import com.kuwarte.api.preferences.enums.ChatProvider;
import com.kuwarte.api.preferences.enums.Theme;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateUserPreferencesRequest(
        Theme theme,
        ChatProvider chatProvider,
        String chatModel,

        @Min(value = 50, message = "chunkSize must be at least 50 characters") @Max(value = 4000, message = "chunkSize must be at most 4000 characters") Integer chunkSize) {
}
