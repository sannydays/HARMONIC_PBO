package com.app.hormontracker.controller;

import com.app.hormontracker.model.mood.MoodEntry;
import com.app.hormontracker.model.mood.MoodEffect; // Import Logic Effect
import com.app.hormontracker.model.mood.MoodType;
import com.app.hormontracker.service.FileService;
import com.app.hormontracker.service.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip; // Import Tooltip
import javafx.scene.layout.StackPane;
import javafx.util.Duration; // Untuk durasi animasi tooltip

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

            Optional<MoodEntry> entryOpt = allEntries.stream()
                    .filter(e -> e.getDate().equals(dateToCheck))
                    .reduce((first, second) -> second); 

            if (entryOpt.isPresent()) {
                MoodEntry entry = entryOpt.get();
                int level = entry.getMoodLevel();
                
                // Buat Data Point
                XYChart.Data<String, Number> data = new XYChart.Data<>(dayName, level);
                
                // Set Custom Node (Titik + Tooltip Effect)
                data.setNode(createCustomNode(entry));

                series.getData().add(data);
            }
        }

        chartWeeklyMood.getData().clear();
        chartWeeklyMood.getData().add(series);
    }

    // Helper: Bikin Titik + Pasang Tooltip MoodEffect
    private StackPane createCustomNode(MoodEntry entry) {
        // 1. Ambil Efek dari Logic MoodEffect
        MoodEffect effect = MoodEffect.create(entry.getType());
        int level = entry.getMoodLevel();
        
        String emoText = effect.emotionalEffect(level);
        String phyText = effect.physicalEffect(level);

        // 2. Bikin Titik (Dot)
        StackPane dot = new StackPane();
        dot.setPrefSize(14, 14);
        dot.setMaxSize(14, 14);
        dot.setStyle("-fx-background-color: white, #ff9ebb; -fx-background-insets: 0, 3; -fx-background-radius: 20px;");

        // 3. Bikin Label Emoji Melayang
        Label emoji = new Label(getEmojiForMood(entry.getType()));
        emoji.setStyle("-fx-font-size: 20px; -fx-font-family: 'Segoe UI Emoji';");
        emoji.setTranslateY(-25); 

        // 4. Gabungin jadi satu container
        StackPane container = new StackPane(dot, emoji);
        container.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        
        // === BAGIAN UTAMA: TOOLTIP AESTHETIC ===
        // Tooltip ini akan muncul saat user hover/klik titiknya
        String tooltipText = String.format(
            "%s Level %d\n\n🧠 %s\n💪 %s\n\n📝 \"%s\"",
            entry.getType().name(), level, emoText, phyText, 
            (entry.getNote().isEmpty() ? "-" : entry.getNote())
        );

        Tooltip tooltip = new Tooltip(tooltipText);
        
        // Styling Tooltip biar Pink & Soft (Bukan kuning default Java)
        tooltip.setStyle(
            "-fx-background-color: #fff0f6; " +
            "-fx-text-fill: #e75480; " +
            "-fx-font-size: 12px; " +
            "-fx-background-radius: 10px; " +
            "-fx-border-color: #ffcce0; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10px; " +
            "-fx-padding: 10px; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        
        tooltip.setShowDelay(Duration.millis(100)); // Muncul cepat
        tooltip.setHideDelay(Duration.millis(200));
        Tooltip.install(container, tooltip);
        // =======================================

        // Mouse Hover Animation
        container.setOnMouseEntered(e -> {
            dot.setStyle("-fx-background-color: #ff9ebb, white; -fx-background-insets: 0, 3; -fx-background-radius: 20px;");
            emoji.setScaleX(1.5); emoji.setScaleY(1.5);
        });
        container.setOnMouseExited(e -> {
            dot.setStyle("-fx-background-color: white, #ff9ebb; -fx-background-insets: 0, 3; -fx-background-radius: 20px;");
            emoji.setScaleX(1.0); emoji.setScaleY(1.0);
        });

        return container;
    }

    private String getEmojiForMood(MoodType type) {
        if (type == null) return "❓";
        return switch (type) {
            case HAPPY -> "🥰";
            case SAD -> "😭";
            case ANGRY -> "🤬";
            case TIRED -> "🫠";
            case ANXIOUS -> "😵‍💫";
            case CRAMPS -> "🩸";
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