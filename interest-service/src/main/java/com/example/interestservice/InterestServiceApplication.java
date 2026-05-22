package com.example.interestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@EnableJms 
@SpringBootApplication
public class InterestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterestServiceApplication.class, args);
    }
}