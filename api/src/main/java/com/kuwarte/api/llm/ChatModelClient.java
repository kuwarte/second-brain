package com.kuwarte.api.llm;

import com.kuwarte.api.preferences.enums.ChatProvider;

public interface ChatModelClient {
    String generate(String prompt, String model);

    ChatProvider provider();
}
