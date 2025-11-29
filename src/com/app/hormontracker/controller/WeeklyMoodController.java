package com.app.hormontracker.controller;

import com.app.hormontracker.model.mood.MoodEntry;
import com.app.hormontracker.model.mood.MoodType; // Pastikan import ini ada
import com.app.hormontracker.service.FileService;
import com.app.hormontracker.service.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane; // Import StackPane

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

public class WeeklyMoodController {

    @FXML private LineChart<String, Number> chartWeeklyMood;
    @FXML private CategoryAxis xAxis; 
    @FXML private Label lblDateRange;

    private final FileService fileService = new FileService();
    private LocalDate currentWeekStart; 

    @FXML
    public void initialize() {
        currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        
        xAxis.setCategories(FXCollections.observableArrayList(
            "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        ));
        
        loadChart();
    }

    @FXML private void prevWeek() {
        currentWeekStart = currentWeekStart.minusWeeks(1);
        loadChart();
    }

    @FXML private void nextWeek() {
        currentWeekStart = currentWeekStart.plusWeeks(1);
        loadChart();
    }

    private void loadChart() {
        String user = SessionManager.getUser().getUsername();
        List<MoodEntry> allEntries = fileService.loadMoods(user);
        LocalDate currentWeekEnd = currentWeekStart.plusDays(6);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d");
        lblDateRange.setText(currentWeekStart.format(fmt) + " - " + currentWeekEnd.format(fmt));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Mood");

        for (int i = 0; i < 7; i++) {
            LocalDate dateToCheck = currentWeekStart.plusDays(i);
            String dayName = dateToCheck.format(DateTimeFormatter.ofPattern("EEE")); 

            Optional<MoodEntry> entry = allEntries.stream()
                    .filter(e -> e.getDate().equals(dateToCheck))
                    .reduce((first, second) -> second); 

            if (entry.isPresent()) {
                int level = entry.get().getMoodLevel();
                MoodType type = entry.get().getType(); // Ambil tipe mood
                
                // Buat Data Point
                XYChart.Data<String, Number> data = new XYChart.Data<>(dayName, level);
                
                // --- BAGIAN AJAIBNYA DI SINI ---
                // Kita set custom node (Titik + Emoji)
                data.setNode(createCustomNode(type));
                // -------------------------------

                series.getData().add(data);
            }
        }

        chartWeeklyMood.getData().clear();
        chartWeeklyMood.getData().add(series);
    }

    // Helper untuk bikin Emoji melayang di atas titik
    private StackPane createCustomNode(MoodType type) {
        // 1. Bikin Titik (Dot) Pink aesthetic
        // Kita pakai StackPane kecil biar mirip CSS .chart-line-symbol
        StackPane dot = new StackPane();
        dot.setPrefSize(14, 14);
        dot.setMaxSize(14, 14);
        // Style manual biar override default css dan pasti pink
        dot.setStyle("-fx-background-color: white, #ff9ebb; -fx-background-insets: 0, 3; -fx-background-radius: 20px;");

        // 2. Bikin Label Emoji
        Label emoji = new Label(getEmojiForMood(type));
        emoji.setStyle("-fx-font-size: 20px; -fx-font-family: 'Segoe UI Emoji';");
        // Geser ke atas (Y negatif) biar melayang di atas titik
        emoji.setTranslateY(-25); 

        // 3. Gabungin dalam satu container transparan
        StackPane container = new StackPane(dot, emoji);
        container.setStyle("-fx-background-color: transparent;");
        
        // PENTING: Mouse hover effect biar interaktif dikit
        container.setOnMouseEntered(e -> {
            dot.setStyle("-fx-background-color: #ff9ebb, white; -fx-background-insets: 0, 3; -fx-background-radius: 20px; -fx-cursor: hand;");
            emoji.setScaleX(1.5); emoji.setScaleY(1.5); // Emoji membesar pas di hover
        });
        container.setOnMouseExited(e -> {
            dot.setStyle("-fx-background-color: white, #ff9ebb; -fx-background-insets: 0, 3; -fx-background-radius: 20px;");
            emoji.setScaleX(1.0); emoji.setScaleY(1.0); // Balik normal
        });

        return container;
    }

    // Helper untuk translate ENUM ke String Emoji
    private String getEmojiForMood(MoodType type) {
        if (type == null) return "❓";
        return switch (type) {
            case HAPPY -> "🥰";
            case SAD -> "😭";
            case ANGRY -> "🤬";
            case TIRED -> "🫠";
            case ANXIOUS -> "😵‍💫";
            case CRAMPS -> "🩸";
            default -> "😶";
        };
    }

    @FXML
    private void backToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
            var stage = (javafx.stage.Stage) chartWeeklyMood.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception ignored) {}
    }
}