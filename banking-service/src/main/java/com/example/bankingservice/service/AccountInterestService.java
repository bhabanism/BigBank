package com.example.bankingservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.bankingservice.dto.request.ApplyInterestRequest;
import com.example.bankingservice.dto.response.ApplyInterestResponse;
import com.example.bankingservice.entity.Account;
import com.example.bankingservice.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountInterestService {

    private final AccountRepository accountRepository;

    @Transactional
    @CacheEvict(value = "accounts", key = "'all'")
    public ApplyInterestResponse applyInterest(String accountNumber, ApplyInterestRequest req) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        if (!"Active".equals(account.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account not active");
        }
        double previous = account.getBalance();
        double updated = previous + req.interestAmount();
        account.setBalance(updated);
        accountRepository.save(account);
        return new ApplyInterestResponse(accountNumber, previous, updated, req.jobId());
    }
}