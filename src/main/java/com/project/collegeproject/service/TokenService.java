package com.project.collegeproject.service;

import java.time.LocalDateTime;

/**
 * TokenService
 */
public class TokenService {

    private final String email;
    private final LocalDateTime expiryTime;

    public TokenService(String email, LocalDateTime expiryTime) {
        this.email = email;
        this.expiryTime = expiryTime;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }
}