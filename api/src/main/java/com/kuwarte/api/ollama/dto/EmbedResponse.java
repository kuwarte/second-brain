package com.kuwarte.api.ollama.dto;

import java.util.List;

public record EmbedResponse(
        List<List<Float>> embeddings) {
}
