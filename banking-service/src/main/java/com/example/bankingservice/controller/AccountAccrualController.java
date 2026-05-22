package com.example.bankingservice.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankingservice.messaging.InterestAccrualProducer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountAccrualController {

    private final InterestAccrualProducer producer;

    @PostMapping("/accrual/run")
    public ResponseEntity<Map<String, String>> runAccrual() {
        String jobId = producer.publishForActiveAccounts();
        return ResponseEntity.accepted().body(Map.of("jobId", jobId, "status", "STARTED"));
    }
}
