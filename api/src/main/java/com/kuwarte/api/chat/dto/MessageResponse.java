package com.kuwarte.api.chat.dto;

import com.kuwarte.api.chat.Message;
import com.kuwarte.api.chat.enums.MessageRole;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        MessageRole role,
        String content,
        List<CitationResponse> citations,
        Instant createdAt) {

    public static MessageResponse toResponse(Message message, List<CitationResponse> citations) {
        return new MessageResponse(
                message.getId(),
                message.getRole(),
                message.getContent(),
                citations,
                message.getCreatedAt());
    }
}
