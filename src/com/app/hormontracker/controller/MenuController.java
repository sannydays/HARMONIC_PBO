package com.app.hormontracker.controller;

import com.app.hormontracker.model.User;
import com.app.hormontracker.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML private Label lblWelcome;
    
    // Button ID sesuai dengan menu.fxml
    @FXML private Button btnMood;
    @FXML private Button btnCalendar;
    @FXML private Button btnCycle;
    @FXML private Button btnGuide;
    @FXML private Button btnWeekly; // ID untuk tombol Weekly Mood
    @FXML private Button btnPeriod; // ID untuk tombol Period Tracker

    @FXML
    public void initialize() {
        // Ambil user yang sedang login untuk set nama
        User currentUser = SessionManager.getUser();
        if (currentUser != null) {
            lblWelcome.setText("Hi, " + currentUser.getUsername() + "! ✨");
        }
    }

    // --- NAVIGATION METHODS ---

    @FXML
    private void navMood() {
        loadView("mood.fxml", btnMood);
    }

    @FXML
    private void navChart() {
        // Mengarah ke file chart aesthetic yang baru dibuat
        loadView("weekly_mood.fxml", btnWeekly);
    }

    @FXML
    private void navCalendar() {
        // Pastikan file calendar.fxml sudah ada, atau komen dulu jika belum
        // loadView("calendar.fxml", btnCalendar);
        loadView("calendar.fxml", btnCalendar);
    }

    @FXML
    private void navCycle() {
        // loadView("cycle.fxml", btnCycle);
        loadView("cycle.fxml", btnCycle);
    }

    @FXML
    private void navGuide() {
        // loadView("guide.fxml", btnGuide);
        loadView("guidebook.fxml", btnGuide);
    }

    @FXML
    private void navPeriod() {
        // loadView("period.fxml", btnPeriod);
        loadView("period.fxml", btnPeriod);
    }

    @FXML
    private void navLogout() {
        // Logout user dan kembali ke login
        SessionManager.logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/login.fxml"));
            // Ambil stage dari label welcome (karena tombol logout mungkin di region lain, tapi label pasti ada)
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- HELPER METHOD ---
    // Method ini dibuat biar gak capek ngetik try-catch berulang kali
    private void loadView(String fxmlFileName, Button sourceButton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/" + fxmlFileName));
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat view: " + fxmlFileName);
        } catch (NullPointerException e) {
            System.out.println("File FXML tidak ditemukan: " + fxmlFileName);
            e.printStackTrace();
        }
    }
}