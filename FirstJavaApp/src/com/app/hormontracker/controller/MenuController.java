package com.app.hormontracker.controller;

import com.app.hormontracker.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {
    @FXML private Label lblWelcome;

    @FXML
    public void initialize() {
        // PERBAIKAN DI SINI:
        // Panggil .getUser() dulu, baru .getName()
        String name = SessionManager.getUser() != null ? SessionManager.getUser().getUsername() : "User";
        
        lblWelcome.setText("Hi, " + name + " ✿");
    }

    @FXML private void navMood() throws IOException { loadPage("mood.fxml"); }
    @FXML private void navCalendar() throws IOException { loadPage("calendar.fxml"); }
    @FXML private void navCycle() throws IOException { loadPage("cycle.fxml"); }
    @FXML private void navGuide() throws IOException { loadPage("guidebook.fxml"); }
    @FXML private void navLogout() throws IOException { loadPage("login.fxml"); }

    private void loadPage(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/" + fxml));
        Stage stage = (Stage) lblWelcome.getScene().getWindow();
        stage.setScene(new Scene(root, 420, 750));
    }
}