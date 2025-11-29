package com.app.hormontracker.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;

public class GuidebookController {

    @FXML private Accordion guideAccordion;

    @FXML
    public void initialize() {
        // Otomatis buka panel pertama saat halaman dibuka
        if (guideAccordion != null && !guideAccordion.getPanes().isEmpty()) {
            guideAccordion.setExpandedPane(guideAccordion.getPanes().get(0));
        }
    }

    @FXML
    private void backToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
            var stage = (javafx.stage.Stage) guideAccordion.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}