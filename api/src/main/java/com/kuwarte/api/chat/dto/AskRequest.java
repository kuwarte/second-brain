package com.kuwarte.api.chat.dto;

import jakarta.validation.constraints.NotBlank;

public record AskRequest(
        @NotBlank String content) {
}
