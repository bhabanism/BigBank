package com.example.bankingservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ApplyInterestRequest(
        @NotBlank String jobId,
        @Positive double interestAmount
) {}