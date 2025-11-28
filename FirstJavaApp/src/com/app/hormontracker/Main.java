package com.app.hormontracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load Login FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/hormontracker/view/login.fxml"));
        Parent root = loader.load();
        
        primaryStage.setTitle("Menstrual Cycle Tracker ✿");
        primaryStage.setScene(new Scene(root, 420, 750));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}