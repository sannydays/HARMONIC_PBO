package com.app.hormontracker.model;

import java.time.LocalDate;

public class User {
    private String username;
    private String password;
    private String email;
    private int cycleLength;
    private LocalDate lastPeriod;

    public User(String username, String password, String email, int cycleLength, LocalDate lastPeriod) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.cycleLength = cycleLength;
        this.lastPeriod = lastPeriod;
    }

    public String getUsername() { 
        return username; 
    }

    public String getPassword() { 
        return password; 
    }

    public String getEmail() { 
        return email; 
    }

    public int getCycleLength() { 
        return cycleLength; 
    }

    public LocalDate getLastPeriod() { 
        return lastPeriod; 
    }
}