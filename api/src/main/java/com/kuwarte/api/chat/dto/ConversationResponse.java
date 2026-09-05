package com.kuwarte.api.chat.dto;

import com.kuwarte.api.chat.Conversation;

import java.time.Instant;
import java.util.UUID;

public record ConversationResponse(
        UUID id,
        String title,
        Instant createdAt) {

    public static ConversationResponse toResponse(Conversation conversation) {
        return new ConversationResponse(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getCreatedAt());
    }
}
