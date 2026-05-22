package com.example.interestservice.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.interestservice.client.BankingAccountClient;
import com.example.interestservice.service.InterestCalculator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InterestAccrualListener {

    private final InterestCalculator calculator;
    private final BankingAccountClient bankingClient;

    @JmsListener(destination = "${app.jms.interest-queue}")
    public void onMessage(InterestAccrualMessage message) {
        double interest = calculator.calculate(message.accountType(), message.balanceAtPublish());
        if (interest <= 0) {
            return;
        }
        bankingClient.applyInterest(message.accountNumber(), message.jobId(), interest);
    }
}