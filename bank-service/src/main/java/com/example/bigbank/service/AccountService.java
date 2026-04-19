package com.example.bigbank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.bigbank.dto.response.AccountSummaryResponse;
import com.example.bigbank.entity.Account;
import com.example.bigbank.repository.AccountRepository;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<AccountSummaryResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll(Sort.by("accountNumber"));        
        return accounts.stream()
                .map(this::getAccountSummary)
                .toList();
    }

    public Double getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(Account::getBalance)
                .orElse(0.0);
    }

    @Transactional(readOnly = true)
    public AccountSummaryResponse findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return getAccountSummary(account);
    }

    private AccountSummaryResponse getAccountSummary(Account account) {
        AccountSummaryResponse accountSummaryResponse = new AccountSummaryResponse();
        accountSummaryResponse.setAccountNumber(account.getAccountNumber());
        accountSummaryResponse.setAccountType(account.getAccountType());
        accountSummaryResponse.setBalance(account.getBalance());
        accountSummaryResponse.setOpenDate(account.getOpenDate());
        accountSummaryResponse.setStatus(account.getStatus());
        accountSummaryResponse.setCustomerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        accountSummaryResponse.setCustomerEmail(account.getCustomer().getEmail());
        accountSummaryResponse.setCustomerPhone(account.getCustomer().getPhone());
        return accountSummaryResponse;
    }
}
