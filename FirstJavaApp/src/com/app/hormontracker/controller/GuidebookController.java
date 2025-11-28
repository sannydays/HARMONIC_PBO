package com.app.hormontracker.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

public class GuidebookController {
    @FXML private Accordion guideAccordion;

    @FXML
    public void initialize() {
        guideAccordion.setExpandedPane(guideAccordion.getPanes().get(0));
    }

    @FXML
    private void back() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
        Stage stage = (Stage) guideAccordion.getScene().getWindow();
        stage.setScene(new Scene(root, 420, 750));
    }
}