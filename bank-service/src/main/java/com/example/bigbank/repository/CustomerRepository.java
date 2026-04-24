package com.example.bigbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bigbank.entity.Customer;

@Repository // This annotation tells Spring this is a DAO layer bean
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAll();
}