package com.example.bankingservice.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingservice.dto.request.ApplyInterestRequest;
import com.example.bankingservice.dto.response.ApplyInterestResponse;
import com.example.bankingservice.service.AccountInterestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/internal/accounts")
@RequiredArgsConstructor
public class InternalAccountController {

    private final AccountInterestService accountInterestService;

    @PatchMapping("/{accountNumber}/interest")
    public ApplyInterestResponse applyInterest(
            @PathVariable String accountNumber,
            @Valid @RequestBody ApplyInterestRequest body) {
        return accountInterestService.applyInterest(accountNumber, body);
    }
}
