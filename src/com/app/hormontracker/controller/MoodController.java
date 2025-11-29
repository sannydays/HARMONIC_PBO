package com.app.hormontracker.controller;

import com.app.hormontracker.model.mood.IntensityMoodEntry;
import com.app.hormontracker.model.mood.MoodType;
import com.app.hormontracker.service.FileService;
import com.app.hormontracker.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane; 
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class MoodController {
    @FXML private VBox viewSelection, viewNote;
    @FXML private Label lblSelectedEmoji;
    @FXML private TextArea taNote;
    @FXML private Slider slIntensity; 
    
    private MoodType selectedMood = null;
    private final FileService fileService = new FileService();

    @FXML public void initialize() { 
        showSelectionView(); 
    }

    @FXML private void pickHappy() { proceed(MoodType.HAPPY, "🥰"); }
    @FXML private void pickSad() { proceed(MoodType.SAD, "😭"); }
    @FXML private void pickAngry() { proceed(MoodType.ANGRY, "🤬"); } 
    @FXML private void pickTired() { proceed(MoodType.TIRED, "🫠"); } 
    @FXML private void pickAnxious() { proceed(MoodType.ANXIOUS, "😵‍💫"); } 
    @FXML private void pickCramps() { proceed(MoodType.CRAMPS, "🩸"); }

    private void proceed(MoodType t, String e) {
        this.selectedMood = t;
        lblSelectedEmoji.setText(e);
        viewSelection.setVisible(false);
        viewNote.setVisible(true);
    }

    @FXML private void saveMood() {
        if (selectedMood == null) 
            return;

        String u = SessionManager.getUser().getUsername();
        int level = (int) slIntensity.getValue(); 

        fileService.appendMood(
            new IntensityMoodEntry(LocalDate.now(), selectedMood, taNote.getText(), level),
            u
        );

        // === UPDATE STYLE ALERT ===
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Mood Saved 💖");
        a.setHeaderText(null);
        a.setContentText("Pssst, your mood Secured! <3");

        // Load CSS ke dalam Alert
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/com/app/hormontracker/view/style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
        
        a.showAndWait();

        backToMenu();
    }
    
    @FXML private void showSelectionView() { 
        viewNote.setVisible(false); 
        viewSelection.setVisible(true); 
    }
    
    @FXML private void backToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
            Stage stage = (Stage) viewSelection.getScene().getWindow();
            stage.getScene().setRoot(root); 
        } catch (Exception e) {}
    }
}