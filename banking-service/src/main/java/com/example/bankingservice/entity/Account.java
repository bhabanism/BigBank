package com.example.bankingservice.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountid")
    private Long id;

    @Column(name = "accountnumber", unique = true, nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "accounttype", nullable = false)
    private String accountType;

    @Column(name = "balance", nullable = false)
    private double balance = 0.00;

    @Column(name = "opendate", nullable = false)
    private LocalDate openDate;

    @Column(name = "status")
    private String status = "Active";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;
}
