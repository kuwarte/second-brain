package com.kuwarte.api.ollama.dto;

public record GenerateResponse(
        String response,
        boolean done) {
}
