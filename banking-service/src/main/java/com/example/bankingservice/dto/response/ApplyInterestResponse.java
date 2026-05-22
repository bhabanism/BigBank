package com.example.bankingservice.dto.response;

public record ApplyInterestResponse(
        String accountNumber,
        double previousBalance,
        double newBalance,
        String jobId
) {}