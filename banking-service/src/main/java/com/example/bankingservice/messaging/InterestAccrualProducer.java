package com.example.bankingservice.messaging;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.example.bankingservice.entity.Account;
import com.example.bankingservice.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterestAccrualProducer {

    private final AccountRepository accountRepository;
    private final JmsTemplate jmsTemplate;

    @Value("${app.jms.interest-queue}")
    private String queueName;

    public String publishForActiveAccounts() {
        String jobId = UUID.randomUUID().toString();
        List<Account> accounts = accountRepository.findAll(Sort.by("accountNumber"));
        for (Account a : accounts) {
            if (!"Active".equals(a.getStatus())) {
                continue;
            }
            var msg = new InterestAccrualMessage(
                    jobId, a.getAccountNumber(), a.getAccountType(), a.getBalance());
            jmsTemplate.convertAndSend(queueName, msg);
        }
        return jobId;
    }
}
