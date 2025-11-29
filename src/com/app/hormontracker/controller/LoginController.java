package com.app.hormontracker.controller;

import com.app.hormontracker.model.User;
import com.app.hormontracker.service.FileService;
import com.app.hormontracker.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class LoginController {
    @FXML private VBox loginContainer, registerContainer;
    @FXML private TextField tfLoginUser, tfRegUser, tfRegEmail, tfRegCycle;
    @FXML private PasswordField pfLoginPass, pfRegPass;
    @FXML private DatePicker dpRegLast;
    @FXML private Label lblLoginMsg, lblRegMsg;

    private final FileService fileService = new FileService();
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"; // Pattern email

    // Menampilkan log in view
    @FXML public void initialize() { 
        showLoginView(); 
    }

    // Untuk ganti ke register kalau blm log in
    @FXML private void switchToRegister() { 
        loginContainer.setVisible(false); registerContainer.setVisible(true); lblRegMsg.setText(""); 
    }

    // Habis regist done -> log in switch lg yaw
    @FXML private void switchToLogin() { 
        registerContainer.setVisible(false); loginContainer.setVisible(true); lblLoginMsg.setText(""); 
    }

    private void showLoginView() { 
        loginContainer.setVisible(true); registerContainer.setVisible(false); 
    }

    @FXML private void actionLogin() {
        String u = tfLoginUser.getText().trim(); // Username
        String p = pfLoginPass.getText().trim(); // Password
        if(u.isEmpty() || p.isEmpty()) { lblLoginMsg.setText("Isi username & password!"); return; }
        
        User user = fileService.authenticate(u, p); // Cek apakah username dan password benar dgn panggil method authenticate
        if (user != null) {
            goToMenu(user); // Menampilkan menu untuk user
        } else {
            lblLoginMsg.setText("Akun tidak ditemukan / Password salah.");
        }
    }

    @FXML private void actionRegister() {
        try {
            String u = tfRegUser.getText().trim();
            String p = pfRegPass.getText().trim();
            String e = tfRegEmail.getText().trim();
            String cStr = tfRegCycle.getText().trim();
            LocalDate d = dpRegLast.getValue();

            if(u.isEmpty() || p.isEmpty() || e.isEmpty() || cStr.isEmpty() || d == null) {
                lblRegMsg.setText("Semua data wajib diisi!"); return;
            }
            if(!Pattern.matches(EMAIL_REGEX, e)) { 
                lblRegMsg.setText("Format email salah!"); return; 
            }

            User newUser = new User(u, p, e, Integer.parseInt(cStr), d);
            fileService.registerUser(newUser);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registrasi Sukses! Silakan login.");
            alert.showAndWait();
            switchToLogin();
            tfLoginUser.setText(u);

        } catch (Exception ex) { 
            lblRegMsg.setText("Error: " + ex.getMessage()); 
        }
    }

    private void goToMenu(User user) {
        try {
            SessionManager.setUser(user);
            Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
            
            Stage stage = (Stage) tfLoginUser.getScene().getWindow();
    
            stage.getScene().setRoot(root);
            
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
}