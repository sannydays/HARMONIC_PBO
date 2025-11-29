package com.app.hormontracker.controller;

import com.app.hormontracker.model.User;
import com.app.hormontracker.service.FileService;
import com.app.hormontracker.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane; 
import javafx.stage.Stage;
import java.time.LocalDate;

public class PeriodController {
    @FXML private DatePicker dpPeriod;
    private final FileService fileService = new FileService();

    @FXML
    public void initialize() {
        User user = SessionManager.getUser();
        if (user != null) {
            dpPeriod.setValue(user.getLastPeriod());
        }
    }

    @FXML
    private void savePeriod() {
        LocalDate date = dpPeriod.getValue();
        if (date == null) return;

        User old = SessionManager.getUser();
        
        User updatedUser = new User(old.getUsername(), old.getPassword(), old.getEmail(), old.getCycleLength(), date);
        fileService.saveUser(updatedUser);
        
        fileService.addPeriodDate(old.getUsername(), date);
        
        SessionManager.setUser(updatedUser);

        // === UPDATE STYLE ALERT ===
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success ✨");
        alert.setHeaderText(null); // Hilangkan header default
        alert.setContentText("Period Logged! 🩸\nCycle Prediction has been updated.");
        
        // Load CSS ke dalam Alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/app/hormontracker/view/style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert"); // Panggil class CSS
        
        alert.showAndWait();

        backToMenu();
    }

    @FXML
    private void backToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
            Stage stage = (Stage) dpPeriod.getScene().getWindow();
            stage.getScene().setRoot(root); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
}