package com.kuwarte.api.ollama;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OllamaConfig {

    @Value("${ollama.base-url}")
    private String baseUrl;

    @Value("${ollama.connect-timeout-seconds:5}")
    private long connectTimeoutSeconds;

    @Value("${ollama.read-timeout-seconds:120}")
    private long readTimeoutSeconds;

    @Bean
    public RestClient ollamaRestClient() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
                .withReadTimeout(Duration.ofSeconds(readTimeoutSeconds));
        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.detect().build(settings);
        return RestClient.builder().baseUrl(baseUrl).requestFactory(requestFactory).build();
    }
}
