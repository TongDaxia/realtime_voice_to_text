package com.tyg.speech.service;

import com.tyg.speech.config.AppConfig;

public class AuthService {
    private final AppConfig config;

    public AuthService(AppConfig config) {
        this.config = config;
    }

    public boolean authenticate(String token) {
        return config.getAuthToken().equals(token);
    }
}