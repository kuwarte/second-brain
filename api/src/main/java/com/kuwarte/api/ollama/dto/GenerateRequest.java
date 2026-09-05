package com.kuwarte.api.ollama.dto;

public record GenerateRequest(
        String model,
        String prompt,
        boolean stream) {

    public static GenerateRequest of(String model, String prompt) {
        return new GenerateRequest(model, prompt, false);
    }
}
