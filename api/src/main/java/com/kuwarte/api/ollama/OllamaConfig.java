package com.kuwarte.api.ollama;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OllamaConfig {

    @Value("${ollama.base-url}")
    private String baseUrl;

    @Bean
    public RestClient ollamaRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
