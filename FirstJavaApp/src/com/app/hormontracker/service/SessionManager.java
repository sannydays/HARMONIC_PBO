package com.app.hormontracker.service;
import com.app.hormontracker.model.User;

public class SessionManager {
    private static User currentUser;
    public static void setUser(User user) { currentUser = user; }
    public static User getUser() { return currentUser; }
}