package com.app.hormontracker.controller;

import com.app.hormontracker.model.User;
import com.app.hormontracker.service.FileService;
import com.app.hormontracker.service.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class LoginController {

    // --- CONTAINERS (UNTUK TUKAR TAMPILAN) ---
    @FXML private VBox loginContainer;
    @FXML private VBox registerContainer;

    // --- LOGIN FIELDS ---
    @FXML private TextField tfLoginUser;
    @FXML private PasswordField pfLoginPass;
    @FXML private Label lblLoginMsg;

    // --- REGISTER FIELDS ---
    @FXML private TextField tfRegUser;
    @FXML private PasswordField pfRegPass;
    @FXML private TextField tfRegEmail;
    @FXML private TextField tfRegCycle;
    @FXML private DatePicker dpRegLast;
    @FXML private Label lblRegMsg;

    private final FileService fileService = new FileService();
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @FXML
    public void initialize() {
        // Default: Tampilkan Login, Sembunyikan Register
        showLoginView();
    }

    // --- NAVIGASI ANTAR VIEW ---
    @FXML
    private void switchToRegister() {
        loginContainer.setVisible(false);
        registerContainer.setVisible(true);
        lblRegMsg.setText(""); // Reset pesan error
    }

    @FXML
    private void switchToLogin() {
        registerContainer.setVisible(false);
        loginContainer.setVisible(true);
        lblLoginMsg.setText(""); // Reset pesan error
    }

    // Helper untuk set visibility
    private void showLoginView() {
        loginContainer.setVisible(true);
        registerContainer.setVisible(false);
    }

    // --- LOGIC LOGIN ---
    @FXML
    private void actionLogin() {
        String u = tfLoginUser.getText();
        String p = pfLoginPass.getText();

        if (u.isBlank() || p.isBlank()) {
            lblLoginMsg.setText("Username/Password kosong!");
            return;
        }

        User user = fileService.authenticate(u, p);
        if (user != null) {
            goToMenu(user);
        } else {
            lblLoginMsg.setText("Username/Password Salah!");
        }
    }

    // --- LOGIC REGISTER ---
    @FXML
    private void actionRegister() {
        try {
            String u = tfRegUser.getText();
            String p = pfRegPass.getText();
            String email = tfRegEmail.getText();
            String cStr = tfRegCycle.getText();
            LocalDate d = dpRegLast.getValue();

            if(u.isBlank() || p.isBlank() || email.isBlank() || cStr.isBlank() || d == null) {
                lblRegMsg.setText("Semua data wajib diisi!");
                return;
            }

            if (!Pattern.matches(EMAIL_REGEX, email)) {
                lblRegMsg.setText("Format email salah!");
                return;
            }

            int c = Integer.parseInt(cStr);
            User newUser = new User(u, p, email, c, d);

            fileService.registerUser(newUser);
            
            // Sukses Register -> Langsung Switch ke Login & Isi fieldnya
            showAlertInfo("Registrasi Berhasil", "Akun berhasil dibuat! Silakan login.");
            switchToLogin();
            tfLoginUser.setText(u); // Auto-fill username
            pfLoginPass.setText("");

        } catch (NumberFormatException e) {
            lblRegMsg.setText("Siklus harus angka!");
        } catch (Exception e) {
            lblRegMsg.setText(e.getMessage());
        }
    }

    private void goToMenu(User user) {
        try {
            SessionManager.setUser(user);
            Parent root = FXMLLoader.load(getClass().getResource("/com/app/hormontracker/view/menu.fxml"));
            Stage stage = (Stage) tfLoginUser.getScene().getWindow();
            stage.setScene(new Scene(root, 420, 750));
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void showAlertInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}