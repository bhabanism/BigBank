package com.example.bigbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bigbank.component.NotificationComponent;
import com.example.bigbank.entity.Account;
import com.example.bigbank.repository.AccountRepository;

@Service   // Business logic layer
public class TransferService {

    private final NotificationComponent notificationComponent;

    @Autowired
    private AccountRepository accountRepository;

    public TransferService(NotificationComponent notificationComponent) {
        this.notificationComponent = notificationComponent;
    }

    @Transactional   // This is the magic! Whole method is atomic
    public void transferMoney(String fromAccountNumber, String toAccountNumber, double amount) {
        Account from = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("From account not found"));
        Account to = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        if (from.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance!");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);

        notificationComponent.sendTransferNotification(fromAccountNumber, toAccountNumber, amount);
        System.out.println("Transfer successful: ₹" + amount);
    }
}