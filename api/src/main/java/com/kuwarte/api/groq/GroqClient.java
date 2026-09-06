package com.kuwarte.api.groq;

import com.kuwarte.api.groq.dto.GroqChatRequest;
import com.kuwarte.api.groq.dto.GroqChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class GroqClient {

    private final RestClient groqRestClient;

    public String generate(String prompt, String model) {
        GroqChatResponse response = groqRestClient.post()
                .uri("/chat/completions")
                .body(GroqChatRequest.of(model, prompt))
                .retrieve()
                .body(GroqChatResponse.class);

        if (response == null || response.choices().isEmpty()) {
            throw new GroqException("No response from Groq /chat/completions");
        }
        return response.choices().get(0).message().content();
    }

    public static class GroqException extends RuntimeException {
        public GroqException(String message) {
            super(message);
        }
    }
}
