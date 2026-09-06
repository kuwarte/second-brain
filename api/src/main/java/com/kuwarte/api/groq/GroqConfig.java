package com.kuwarte.api.groq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class GroqConfig {

    @Value("${groq.base-url}")
    private String baseUrl;

    @Value("${groq.api-key}")
    private String apiKey;

    @Value("${groq.connect-timeout-seconds:5}")
    private long connectTimeoutSeconds;

    @Value("${groq.read-timeout-seconds:60}")
    private long readTimeoutSeconds;

    @Bean
    public RestClient groqRestClient() {
        HttpClientSettings settings = HttpClientSettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(connectTimeoutSeconds))
                .withReadTimeout(Duration.ofSeconds(readTimeoutSeconds));

        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.detect().build(settings);

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
