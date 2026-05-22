package com.example.bankingservice.messaging; // interest: com.example.interestservice.messaging

public record InterestAccrualMessage(
        String jobId,
        String accountNumber,
        String accountType,
        double balanceAtPublish
) {}