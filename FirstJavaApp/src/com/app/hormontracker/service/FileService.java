package com.app.hormontracker.service;

import com.app.hormontracker.model.User;
import com.app.hormontracker.model.mood.MoodEntry;
import com.app.hormontracker.model.mood.MoodType;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private final Path DATA_DIR = Path.of("data");
    private final Path USER_FILE = DATA_DIR.resolve("users.txt");
    private final Path MOOD_FILE = DATA_DIR.resolve("moods.txt");

    public FileService() {
        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- AUTHENTICATION ---

    public void registerUser(User user) throws Exception {
        if (checkUserExists(user.getUsername())) {
            throw new Exception("Username sudah terpakai!");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(USER_FILE, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            // Kita Trim (hapus spasi depan belakang) sebelum simpan biar bersih
            String data = String.format("%s|%s|%s|%d|%s",
                    user.getUsername().trim(), 
                    user.getPassword().trim(), 
                    user.getEmail().trim(), 
                    user.getCycleLength(), 
                    user.getLastPeriod());
            
            writer.write(data);
            writer.newLine();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public User authenticate(String inputUser, String inputPass) {
        // Bersihkan input user dari spasi tidak sengaja
        String cleanUser = inputUser.trim();
        String cleanPass = inputPass.trim();

        if (!Files.exists(USER_FILE)) {
            System.out.println("DEBUG: File users.txt belum ada.");
            return null;
        }

        try (BufferedReader reader = Files.newBufferedReader(USER_FILE)) {
            String line;
            int rowNum = 0;
            while ((line = reader.readLine()) != null) {
                rowNum++;
                // Skip baris kosong
                if (line.trim().isEmpty()) continue;

                String[] p = line.split("\\|");
                
                // Format Baru: username|password|email|cycleLength|lastPeriod (5 kolom)
                if (p.length < 5) {
                    System.out.println("DEBUG: Baris " + rowNum + " rusak/format lama. Skip.");
                    continue; 
                }

                String dbUser = p[0].trim();
                String dbPass = p[1].trim();

                // Debugging: Muncul di console bawah IDE kamu
                System.out.println("Cek Baris " + rowNum + ": DB='" + dbUser + "' vs Input='" + cleanUser + "'");

                if (dbUser.equals(cleanUser) && dbPass.equals(cleanPass)) {
                    System.out.println("DEBUG: LOGIN SUKSES!");
                    try {
                        return new User(p[0], p[1], p[2], Integer.parseInt(p[3].trim()), LocalDate.parse(p[4].trim()));
                    } catch (Exception e) {
                        System.out.println("DEBUG: Data user rusak (tanggal/angka salah).");
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        System.out.println("DEBUG: Tidak ada user yang cocok sampai baris terakhir.");
        return null;
    }

    private boolean checkUserExists(String username) {
        if (!Files.exists(USER_FILE)) return false;
        try (BufferedReader reader = Files.newBufferedReader(USER_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length > 0 && p[0].trim().equals(username.trim())) return true;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    // --- MOOD (Tetap Sama) ---
    public void appendMood(MoodEntry mood) {
        try (BufferedWriter writer = Files.newBufferedWriter(MOOD_FILE, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(mood.getDate() + "|" + mood.getMood() + "|" + mood.getNote().replace("\n", "\\n"));
            writer.newLine();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public List<MoodEntry> loadMoods() {
        List<MoodEntry> list = new ArrayList<>();
        if (!Files.exists(MOOD_FILE)) return list;
        try (BufferedReader reader = Files.newBufferedReader(MOOD_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length >= 2) {
                    String note = p.length > 2 ? p[2].replace("\\n", "\n") : "";
                    try {
                        list.add(new MoodEntry(LocalDate.parse(p[0]), MoodType.valueOf(p[1]), note));
                    } catch (Exception e) { /* Skip error lines */ }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}