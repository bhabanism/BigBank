package com.example.interestservice.client;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BankingAccountClient {

    private final RestClient bankingRestClient; // bean below

    public void applyInterest(String accountNumber, String jobId, double interestAmount) {
        var body = Map.of("jobId", jobId, "interestAmount", interestAmount);
        bankingRestClient.patch()
                .uri("/api/internal/accounts/{accountNumber}/interest", accountNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new IllegalStateException("Banking apply interest failed: " + res.getStatusCode());
                })
                .toBodilessEntity();
    }
}
