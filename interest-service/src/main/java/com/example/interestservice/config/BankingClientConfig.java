package com.example.interestservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BankingClientConfig {
    @Bean
    RestClient bankingRestClient(
            @Value("${app.banking.base-url}") String baseUrl,
            @Value("${app.internal.api-key}") String apiKey) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-Internal-Api-Key", apiKey)
                .build();
    }
}
