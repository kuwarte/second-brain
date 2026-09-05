package com.kuwarte.api.ollama;

import com.kuwarte.api.ollama.dto.EmbedRequest;
import com.kuwarte.api.ollama.dto.EmbedResponse;
import com.kuwarte.api.ollama.dto.GenerateRequest;
import com.kuwarte.api.ollama.dto.GenerateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OllamaClient {

    private final RestClient ollamaRestClient;

    @Value("${ollama.embedding-model}")
    private String embeddingModel;

    @Value("${ollama.chat-model}")
    private String chatModel;

    public List<List<Float>> embed(List<String> texts) {
        EmbedResponse response = ollamaRestClient.post()
                .uri("/api/embed")
                .body(new EmbedRequest(embeddingModel, texts))
                .retrieve()
                .body(EmbedResponse.class);

        if (response == null) {
            throw new OllamaException("No response from Ollama /api/embed");
        }
        return response.embeddings();
    }

    public List<Float> embedOne(String text) {
        return embed(List.of(text)).get(0);
    }

    public String generate(String prompt) {
        return generate(prompt, chatModel);
    }

    public String generate(String prompt, String model) {
        GenerateResponse response = ollamaRestClient.post()
                .uri("/api/generate")
                .body(GenerateRequest.of(model, prompt))
                .retrieve()
                .body(GenerateResponse.class);

        if (response == null) {
            throw new OllamaException("No response from Ollama /api/generate");
        }
        return response.response();
    }

    public static class OllamaException extends RuntimeException {
        public OllamaException(String message) {
            super(message);
        }
    }
}
