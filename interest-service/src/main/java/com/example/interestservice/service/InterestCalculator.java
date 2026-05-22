package com.example.interestservice.service;

import org.springframework.stereotype.Component;

@Component
public class InterestCalculator {
    public double calculate(String accountType, double balance) {
        double rate = switch (accountType) {
            case "Savings" -> 0.02;
            case "Checking" -> 0.005;
            default -> 0.0;
        };
        return balance * rate;
    }
}