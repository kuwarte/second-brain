package com.kuwarte.api.profile.dto;

import jakarta.validation.constraints.Email;

public record UpdateProfileRequest(
        @Email String email) {
}
