package com.app.hormontracker.controller;

import com.app.hormontracker.model.User;
import com.app.hormontracker.service.LogicService;
import com.app.hormontracker.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.time.LocalDate;

public class CycleController {
    @FXML private Label lblDay, lblPhase, lblDetail;
    @FXML private Circle indicatorCircle;

    @FXML
    public void initialize() {
        User user = SessionManager.getUser();
        if(user == null) {
            lblDetail.setText("Silakan login ulang.");
            return;
        }

        long day = LogicService.getDayInCycle(user.getLastPeriod(), user.getCycleLength());
        LogicService.Phase phase = LogicService.predictPhase(user.getLastPeriod(), user.getCycleLength(), LocalDate.now());

        lblDay.setText("Hari ke-" + day);
        lblPhase.setText(phase.name());

        switch(phase) {
            case MENSTRUATION -> {
                lblDetail.setText("Fase Menstruasi: Istirahat yang cukup, minum air hangat.");
                indicatorCircle.setFill(Color.web("#ffb6c1"));
            }
            case FOLLICULAR -> {
                lblDetail.setText("Fase Folikular: Energi mulai naik! Waktu tepat untuk olahraga.");
                indicatorCircle.setFill(Color.web("#e6e6fa"));
            }
            case OVULATION -> {
                lblDetail.setText("Ovulasi: Kesuburan puncak. Mood biasanya sangat baik.");
                indicatorCircle.setFill(Color.web("#fffacd"));
            }
            case LUTEAL -> {
                lblDetail.setText("Fase Luteal: Progesteron naik. Waspada gejala PMS.");
                indicatorCircle.setFill(Color.web("#f0fff0"));
            }
        }
    }

    @FXML
    private void back() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
        Stage stage = (Stage) lblDay.getScene().getWindow();
        stage.setScene(new Scene(root, 420, 750));
    }
}