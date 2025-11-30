package com.app.hormontracker.controller;

import com.app.hormontracker.model.mood.MoodEntry;
import com.app.hormontracker.model.mood.MoodType;
import com.app.hormontracker.service.FileService;
import com.app.hormontracker.service.SessionManager;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarController {

    @FXML private StackPane rootStackPane; 
    @FXML private StackPane dialogOverlay; 
    @FXML private GridPane calendarGrid;
    @FXML private Label lblMonthYear;

    private YearMonth currentYearMonth;
    private final FileService fileService = new FileService();

    // Declare style
    private final String STYLE_CELL_BASE = "-fx-background-radius: 16; -fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 4, 0, 0, 2); -fx-cursor: hand;";
    private final String STYLE_CELL_PERIOD = "-fx-background-radius: 16; -fx-background-color: linear-gradient(to bottom right, #ff9a9e, #fad0c4);";
    private final String STYLE_CELL_MOOD = "-fx-background-radius: 16; -fx-background-color: #fff0f5; -fx-border-color: #ffcce0; -fx-border-radius: 16; -fx-border-width: 1.5;";
    private final String STYLE_CELL_TODAY = "-fx-border-color: #e75480; -fx-border-width: 2; -fx-border-style: dashed;";

    @FXML
    public void initialize() {
        if (dialogOverlay != null) {
            // Dialog pop up
            dialogOverlay.setOnMouseClicked(e -> closePopup());
            dialogOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
        }
        
        setupGridConstraints();
        currentYearMonth = YearMonth.now();
        updateCalendar();
    }

    private void setupGridConstraints() {
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();

        // Buat 7 kolom untuk hari
        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 7);
            calendarGrid.getColumnConstraints().add(col);
        }

        // Buat 6 Baris untuk tanggal
        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(85); 
            calendarGrid.getRowConstraints().add(row);
        }
    }

    @FXML private void prevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }

    private void updateCalendar() {
        // Menampilkan bulan dan tahun saat ini
        lblMonthYear.setText(currentYearMonth.getMonth().name() + " " + currentYearMonth.getYear());
        calendarGrid.getChildren().clear();

        String username = SessionManager.getUser().getUsername();
        List<MoodEntry> moods = fileService.loadMoods(username);
        List<LocalDate> periods = fileService.loadPeriodDates(username);

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        
        // Atur agar kalender mulai dari hari yang tepat (dari minggu)
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue(); 
        int startCol = dayOfWeekValue % 7; // Konversi Minggu = 0

        int daysInMonth = currentYearMonth.lengthOfMonth(); // Hitung jml hari pada bulan tsb
        int row = 0;
        int col = startCol;

        // Menampilkan kolom hari
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            StackPane cell = new StackPane();
            
            boolean isPeriod = periods.contains(date);
            boolean isToday = date.equals(LocalDate.now());
            MoodEntry mood = moods.stream().filter(m -> m.getDate().equals(date)).findFirst().orElse(null);

            applyCellStyle(cell, isPeriod, mood != null, isToday);

            VBox content = new VBox(2);
            content.setAlignment(Pos.CENTER);
            
            Label lblDate = new Label(String.valueOf(day));
            lblDate.setStyle("-fx-font-weight: bold; -fx-text-fill: " + (isPeriod ? "white" : "#888") + ";");
            
            Label lblIcon = new Label();
            if (isPeriod) lblIcon.setText("💧");
            else if (mood != null) lblIcon.setText(getEmoji(mood.getType()));
            lblIcon.setStyle("-fx-font-size: 18px;");

            content.getChildren().addAll(lblDate, lblIcon);
            cell.getChildren().add(content);

            cell.setOnMouseClicked(e -> showCustomPopup(date, mood, isPeriod));

            calendarGrid.add(cell, col, row);

            col++;
            if (col > 6) { col = 0; row++; }
        }
    }

    private void applyCellStyle(StackPane cell, boolean isPeriod, boolean hasMood, boolean isToday) {
        String style = STYLE_CELL_BASE;
        if (isPeriod) style += STYLE_CELL_PERIOD;
        else if (hasMood) style += STYLE_CELL_MOOD;
        if (isToday) style += STYLE_CELL_TODAY;
        cell.setStyle(style);
    }

    // Custom pop up saat klik tanggal
    private void showCustomPopup(LocalDate date, MoodEntry mood, boolean isPeriod) {
        dialogOverlay.getChildren().clear();

        VBox dialogBox = new VBox(8); 
        dialogBox.setAlignment(Pos.CENTER);
        
        dialogBox.setMaxWidth(220); 
        dialogBox.setMaxHeight(Region.USE_PREF_SIZE);

        dialogBox.setStyle(
            "-fx-background-color: #fff5f8;" + 
            "-fx-background-radius: 20;" +
            "-fx-border-color: #ff9ac1;" +
            "-fx-border-width: 2px;" +
            "-fx-border-style: dashed;" + 
            "-fx-border-radius: 20;" +
            "-fx-padding: 15;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 4);"
        );

        // Header Tanggal
        Label lblDate = new Label(date.format(DateTimeFormatter.ofPattern("MMM dd")));
        lblDate.setStyle("-fx-text-fill: #ff85a2; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Emoji mood 
        Label lblEmoji = new Label();
        String emoji = (mood != null) ? getEmoji(mood.getType()) : "✨";
        if (isPeriod) emoji = "🩸"; // Override jika period
        lblEmoji.setText(emoji);
        lblEmoji.setStyle("-fx-font-size: 40px; -fx-font-family: 'Segoe UI Emoji', 'Apple Color Emoji'; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");

        // Status Badge (Period / Mood Name)
        Label lblStatus = new Label();
        if (isPeriod) {
            lblStatus.setText("Period Day");
            lblStatus.setStyle("-fx-background-color: #ff9ac1; -fx-text-fill: white; -fx-padding: 3 12; -fx-background-radius: 100; -fx-font-size: 11px; -fx-font-weight: bold;");
        } else if (mood != null) {
            lblStatus.setText(mood.getType().name());
            lblStatus.setStyle("-fx-background-color: #bcd2ee; -fx-text-fill: white; -fx-padding: 3 12; -fx-background-radius: 100; -fx-font-size: 11px; -fx-font-weight: bold;");
        } else {
            lblStatus.setText("Nothing Logged");
            lblStatus.setStyle("-fx-text-fill: #bbb; -fx-font-size: 11px; -fx-font-style: italic;");
        }

        // Menampilkan note (Jika ada)
        VBox noteContainer = new VBox();
        noteContainer.setAlignment(Pos.CENTER);
        if (mood != null && !mood.getNote().isEmpty()) {
            Label lblNote = new Label("“" + mood.getNote() + "”");
            lblNote.setWrapText(true);
            lblNote.setMaxWidth(190);
            lblNote.setStyle("-fx-text-fill: #666; -fx-font-size: 11px; -fx-font-style: italic; -fx-text-alignment: CENTER;");
            
            // Garis pemisah 
            Region line = new Region();
            line.setMaxWidth(100);
            line.setPrefHeight(1);
            line.setStyle("-fx-background-color: #ffcce0;");
            
            noteContainer.getChildren().addAll(line, lblNote);
            noteContainer.setSpacing(5);
        }

        // Tombol Close 
        Button btnClose = new Button("Checked!");
        btnClose.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #ff85a2;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 12px;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 5 10;"
        );
        
        btnClose.setOnMouseEntered(e -> btnClose.setStyle("-fx-background-color: #fff0f5; -fx-text-fill: #ff6b81; -fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 10; -fx-cursor: hand;"));
        btnClose.setOnMouseExited(e -> btnClose.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff85a2; -fx-font-weight: bold; -fx-font-size: 12px; -fx-cursor: hand;"));
        
        btnClose.setOnAction(e -> closePopup());
        
        // Add children
        dialogBox.getChildren().addAll(lblDate, lblEmoji, lblStatus);
        if (!noteContainer.getChildren().isEmpty()) {
            dialogBox.getChildren().add(noteContainer);
        }
        dialogBox.getChildren().add(btnClose);
        
        dialogOverlay.getChildren().add(dialogBox);
        dialogOverlay.setVisible(true);

        // Animation
        ScaleTransition st = new ScaleTransition(Duration.millis(500), dialogBox);
        st.setFromX(0.5); st.setFromY(0.5);
        st.setToX(1.0); st.setToY(1.0);
        st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        st.play();
    }

    // Dialogpane ditutup
    private void closePopup() {
        dialogOverlay.setVisible(false);
    }

    // Emoji sesuai mood saat itu
    private String getEmoji(MoodType t) {
        if (t == null) {
            return "😶";
        }
        return switch (t) {
            case HAPPY -> "🥰";
            case SAD -> "😭";
            case ANGRY -> "🤬";
            case TIRED -> "🫠";
            case ANXIOUS -> "😵‍💫";
            case CRAMPS -> "🩸";
        };
    }

    // kembali ke main menu
    @FXML private void back() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
        Stage stage = (Stage) calendarGrid.getScene().getWindow();
        stage.getScene().setRoot(root); 
    }
}