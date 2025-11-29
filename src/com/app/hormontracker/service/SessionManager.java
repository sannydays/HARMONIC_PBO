package com.app.hormontracker.service;

import com.app.hormontracker.model.User;

public class SessionManager {
    private static User currentUser;

    // Method untuk set user saat login
    public static void setUser(User user) { 
        currentUser = user; 
    }

    // Method untuk ambil data user
    public static User getUser() { 
        return currentUser; 
    }

    // TAMBAHAN: Method logout untuk menghapus sesi
    public static void logout() {
        currentUser = null;
    }
}