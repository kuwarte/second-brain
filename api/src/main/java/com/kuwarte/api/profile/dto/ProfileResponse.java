package com.kuwarte.api.profile.dto;

import com.kuwarte.api.auth.User;

import java.time.Instant;
import java.util.UUID;

public record ProfileResponse(
        UUID id,
        String email,
        Instant createdAt) {
    public static ProfileResponse toResponse(User user) {
        return new ProfileResponse(user.getId(), user.getEmail(), user.getCreatedAt());
    }
}
