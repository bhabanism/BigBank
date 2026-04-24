package com.example.bigbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.bigbank.component.NotificationComponent;
import com.example.bigbank.service.TransferService;

@Configuration
public class AppConfig {

    @Bean   // Explicitly registering a bean (you can use this pattern for 3rd-party objects too)
    public String welcomeMessage() {
        return "🏦 Welcome to Spring Bank Transfer Demo!";
    }

    @Bean
    public NotificationComponent notificationComponent() {
        return new NotificationComponent();
    }

    @Bean
    public TransferService transferService(NotificationComponent notificationComponent) {
        return new TransferService(notificationComponent);
    }
}