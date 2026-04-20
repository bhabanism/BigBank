package com.example.bigbank.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigbank.dto.response.AccountSummaryResponse;
import com.example.bigbank.entity.Customer;
import com.example.bigbank.service.AccountService;
import com.example.bigbank.service.CustomerService;

@RestController // Combines @Controller + @ResponseBody
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class BankController {

    private final CustomerService customerService;
    private final AccountService accountService;

    public BankController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

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