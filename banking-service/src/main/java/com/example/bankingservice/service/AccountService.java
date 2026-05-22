package com.example.bankingservice.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.bankingservice.dto.response.AccountSummaryResponse;
import com.example.bankingservice.entity.Account;
import com.example.bankingservice.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "accounts", key = "'all'")
    public List<AccountSummaryResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll(Sort.by("accountNumber"));
        return accounts.stream().map(this::getAccountSummary).toList();
    }

    @Transactional(readOnly = true)
    public double getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(Account::getBalance)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    @Transactional(readOnly = true)
    public AccountSummaryResponse findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return getAccountSummary(account);
    }

    private AccountSummaryResponse getAccountSummary(Account account) {
        AccountSummaryResponse summary = new AccountSummaryResponse();
        summary.setAccountNumber(account.getAccountNumber());
        summary.setAccountType(account.getAccountType());
        summary.setBalance(account.getBalance());
        summary.setOpenDate(account.getOpenDate());
        summary.setStatus(account.getStatus());
        summary.setCustomerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        summary.setCustomerEmail(account.getCustomer().getEmail());
        summary.setCustomerPhone(account.getCustomer().getPhone());
        return summary;
    }
}
