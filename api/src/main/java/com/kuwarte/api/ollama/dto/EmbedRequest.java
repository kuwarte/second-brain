package com.kuwarte.api.ollama.dto;

import java.util.List;

public record EmbedRequest(
        String model,
        List<String> input) {
}
