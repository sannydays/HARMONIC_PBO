package com.app.hormontracker.controller;

import com.app.hormontracker.model.mood.MoodEntry;
import com.app.hormontracker.service.FileService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarController {
    @FXML private GridPane calendarGrid;
    @FXML private Label lblMonthYear;
    private final FileService fileService = new FileService();

    @FXML
    public void initialize() {
        buildCalendar(YearMonth.now());
    }

    private void buildCalendar(YearMonth ym) {
        lblMonthYear.setText(ym.getMonth().name() + " " + ym.getYear());
        calendarGrid.getChildren().clear();
        
        List<MoodEntry> moods = fileService.loadMoods();
        LocalDate firstOfMonth = ym.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Mon
        int daysInMonth = ym.lengthOfMonth();

        int col = (dayOfWeek % 7); // Adjust so Sun is last or first based on pref
        int row = 0;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = ym.atDay(day);
            StackPane cell = new StackPane();
            cell.setStyle("-fx-background-color: white; -fx-border-color: #ffe0f0; -fx-border-radius: 5;");
            
            Label dayNum = new Label(String.valueOf(day));
            
            // Cek Mood
            for(MoodEntry m : moods) {
                if(m.getDate().equals(date)) {
                    cell.setStyle("-fx-background-color: #fff0f5; -fx-border-color: #ff69b4;");
                    dayNum.setText(day + getEmoji(m));
                }
            }
            
            cell.getChildren().add(dayNum);
            calendarGrid.add(cell, col, row);

            col++;
            if (col > 6) { col = 0; row++; }
        }
    }

    private String getEmoji(MoodEntry m) {
        return switch (m.getMood()) {
            case HAPPY -> " 😊";
            case SAD -> " 😢";
            case IRRITABLE -> " 😡";
            case CRAMPS -> " 🤕";
            default -> " 😐";
        };
    }

    @FXML
    private void back() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
        Stage stage = (Stage) calendarGrid.getScene().getWindow();
        stage.setScene(new Scene(root, 420, 750));
    }
}