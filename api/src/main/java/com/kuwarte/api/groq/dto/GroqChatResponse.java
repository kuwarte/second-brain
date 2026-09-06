package com.kuwarte.api.groq.dto;

import java.util.List;

public record GroqChatResponse(List<Choice> choices) {
    public record Choice(GroqMessage message) {
    }
}
