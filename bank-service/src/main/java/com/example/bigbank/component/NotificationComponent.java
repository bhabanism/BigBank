package com.example.bigbank.component;

import org.springframework.stereotype.Component;

@Component   // This is a plain utility bean that we inject elsewhere
public class NotificationComponent {

    public void sendTransferNotification(String fromAccount, String toAccount, double amount) {
        System.out.println("✅ NOTIFICATION: ₹" + amount + " transferred from " + fromAccount + " to " + toAccount);
        // In real life you could send email/SMS here
    }
}