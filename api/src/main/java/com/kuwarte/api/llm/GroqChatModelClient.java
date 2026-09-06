package com.kuwarte.api.llm;

import com.kuwarte.api.groq.GroqClient;
import com.kuwarte.api.preferences.enums.ChatProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroqChatModelClient implements ChatModelClient {

    private final GroqClient groqClient;

    @Override
    public String generate(String prompt, String model) {
        return groqClient.generate(prompt, model);
    }

    @Override
    public ChatProvider provider() {
        return ChatProvider.GROQ;
    }
}
