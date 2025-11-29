package com.app.hormontracker.controller;

import com.app.hormontracker.model.User;
import com.app.hormontracker.service.LogicService;
import com.app.hormontracker.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.LocalDate;

public class CycleController {
    @FXML private Label lblDay, lblPhase, lblDetail;
    @FXML private Circle indicatorCircle;

    @FXML public void initialize() { 
        refresh(); 
    }

    private void refresh() {
        User u = SessionManager.getUser(); // Mengambil user yang sedang log in (kayak siapa gitu)
        if(u == null) 
            return;

        // Inisialisasi hari ke berapa di fase ini gimtu guys
        long day = LogicService.getDayInCycle(u.getLastPeriod(), u.getCycleLength());
        // ini tuh di fase apa? panggil method di logicservice
        LogicService.Phase phase = LogicService.predictPhase(u.getLastPeriod(), u.getCycleLength(), LocalDate.now());

        // Menampilkan hari ke - dan fase terus text dikit
        lblDay.setText("Day " + day);
        lblPhase.setText(phase.name());

        switch(phase) {
            case MENSTRUATION -> { lblDetail.setText("Estrogen low. Rest well! 🛌"); indicatorCircle.setFill(Color.web("#f48fb1")); }
            case FOLLICULAR -> { lblDetail.setText("Energy rising! Go workout! 🏃‍♀️"); indicatorCircle.setFill(Color.web("#e1bee7")); }
            case OVULATION -> { lblDetail.setText("Peak fertility. You glow girl! ✨"); indicatorCircle.setFill(Color.web("#fff9c4")); }
            case LUTEAL -> { lblDetail.setText("PMS zone. Eat chocolate! 🍫"); indicatorCircle.setFill(Color.web("#b2dfdb")); }
        }
    }

    @FXML private void back() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
        Stage stage = (Stage) lblDay.getScene().getWindow();
        stage.getScene().setRoot(root); 
    }
}