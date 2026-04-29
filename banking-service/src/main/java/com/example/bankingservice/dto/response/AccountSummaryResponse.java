package com.example.bankingservice.dto.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountSummaryResponse {

    private String accountNumber;
    private String accountType;
    private double balance;
    private LocalDate openDate;
    private String status;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}
