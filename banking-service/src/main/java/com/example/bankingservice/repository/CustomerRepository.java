package com.example.bankingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankingservice.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
