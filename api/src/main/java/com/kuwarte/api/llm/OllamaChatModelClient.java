package com.kuwarte.api.llm;

import com.kuwarte.api.ollama.OllamaClient;
import com.kuwarte.api.preferences.enums.ChatProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OllamaChatModelClient implements ChatModelClient {

    private final OllamaClient ollamaClient;

    @Override
    public String generate(String prompt, String model) {
        return ollamaClient.generate(prompt, model);
    }

    @Override
    public ChatProvider provider() {
        return ChatProvider.OLLAMA;
    }
}
