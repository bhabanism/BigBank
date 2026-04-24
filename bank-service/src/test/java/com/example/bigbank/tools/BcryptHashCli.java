package com.example.bigbank.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class BcryptHashCli {

    private BcryptHashCli() {}

    public static void main(String[] args) {
        String raw = args.length > 0 ? args[0] : "ChangeMe!";
        int strength = args.length > 1 ? Integer.parseInt(args[1]) : 12;
        String hash = new BCryptPasswordEncoder(strength).encode(raw);
        System.out.println(hash);
    }
}