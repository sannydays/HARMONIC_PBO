package com.app.hormontracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/app/hormontracker/view/login.fxml"));
        Parent root = loader.load();
        
        primaryStage.setTitle("Hormone Tracker ✨");
        
        // Ukuran awal (phone)
        Scene scene = new Scene(root, 450, 800);
        primaryStage.setScene(scene);
        
        // Set bisa resizeable
        primaryStage.setResizable(true);
        
        // Set minimum size
        primaryStage.setMinWidth(400); 
        primaryStage.setMinHeight(600);
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}