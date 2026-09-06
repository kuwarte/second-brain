package com.kuwarte.api.groq.dto;

import java.util.List;

public record GroqChatRequest(String model, List<GroqMessage> messages, boolean stream) {
    public static GroqChatRequest of(String model, String prompt) {
        return new GroqChatRequest(model, List.of(GroqMessage.user(prompt)), false);
    }
}
