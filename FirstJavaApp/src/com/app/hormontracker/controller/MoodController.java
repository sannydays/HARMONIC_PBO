package com.app.hormontracker.controller;

import com.app.hormontracker.model.mood.MoodEntry;
import com.app.hormontracker.model.mood.MoodType;
import com.app.hormontracker.service.FileService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.time.LocalDate;

public class MoodController {
    @FXML private TextArea taNote;
    @FXML private Label lblStatus;
    
    private MoodType selectedMood = MoodType.NEUTRAL;
    private final FileService fileService = new FileService();

    @FXML private void setHappy() { select(MoodType.HAPPY, "Senang 😊"); }
    @FXML private void setSad() { select(MoodType.SAD, "Sedih 😢"); }
    @FXML private void setAngry() { select(MoodType.IRRITABLE, "Marah 😡"); }
    @FXML private void setStressed() { select(MoodType.ANXIOUS, "Cemas 😣"); }
    @FXML private void setCramps() { select(MoodType.CRAMPS, "Kram 🤕"); }

    private void select(MoodType type, String text) {
        this.selectedMood = type;
        lblStatus.setText("Mood: " + text);
    }

    @FXML
    private void saveMood() {
        MoodEntry entry = new MoodEntry(LocalDate.now(), selectedMood, taNote.getText());
        fileService.appendMood(entry);
        lblStatus.setText("Tersimpan! ✅");
        taNote.clear();
    }

    @FXML
    private void back() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.setScene(new Scene(root, 420, 750));
    }
}