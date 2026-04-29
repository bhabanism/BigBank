package com.example.bankingservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingservice.dto.response.AccountSummaryResponse;
import com.example.bankingservice.entity.Customer;
import com.example.bankingservice.service.AccountService;
import com.example.bankingservice.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BankController {

    private final CustomerService customerService;
    private final AccountService accountService;

    @GetMapping("/accounts/{accountNumber}")
    public AccountSummaryResponse getAccount(@PathVariable String accountNumber) {
        return accountService.findByAccountNumber(accountNumber);
    }

    @GetMapping("/accounts/{accountNumber}/balance")
    public double getAccountBalance(@PathVariable String accountNumber) {
        return accountService.getBalance(accountNumber);
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/accounts")
    public List<AccountSummaryResponse> getAllAccounts() {
        return accountService.getAllAccounts();
    }
}
