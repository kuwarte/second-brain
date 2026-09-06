package com.kuwarte.api.groq.dto;

public record GroqMessage(String role, String content) {
    public static GroqMessage user(String content) {
        return new GroqMessage("user", content);
    }
}
