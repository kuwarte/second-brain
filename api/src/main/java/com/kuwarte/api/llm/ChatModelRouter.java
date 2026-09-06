package com.kuwarte.api.llm;

import com.kuwarte.api.preferences.enums.ChatProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ChatModelRouter {

    private final Map<ChatProvider, ChatModelClient> clientsByProvider;

    public ChatModelRouter(List<ChatModelClient> clients) {
        this.clientsByProvider = clients.stream()
                .collect(Collectors.toMap(ChatModelClient::provider, Function.identity()));
    }

    public String generate(ChatProvider provider, String prompt, String model) {
        ChatModelClient client = clientsByProvider.get(provider);
        if (client == null) {
            throw new IllegalStateException("No ChatModelClient registered for provider: " + provider);
        }
        return client.generate(prompt, model);
    }
}
