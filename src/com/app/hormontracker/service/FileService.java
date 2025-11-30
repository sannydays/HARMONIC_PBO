package com.app.hormontracker.service;

import com.app.hormontracker.model.User;
import com.app.hormontracker.model.mood.MoodEntry;
import com.app.hormontracker.model.mood.IntensityMoodEntry; // Import Subclass
import com.app.hormontracker.model.mood.MoodType;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private final Path DATA_DIR = Path.of("data");

    public FileService() {
        try {
            if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }

    private Path getProfilePath(String username) {
        return DATA_DIR.resolve(username + "_profile.txt"); 
    }

    private Path getMoodPath(String username) { 
        return DATA_DIR.resolve(username + "_moods.txt"); 
    }

    private Path getPeriodPath(String username) { 
        return DATA_DIR.resolve(username + "_periods.txt"); 
    }

    // Cek user & Register
    public void registerUser(User user) throws Exception {
        Path p = getProfilePath(user.getUsername());
        if (Files.exists(p)) throw new Exception("Username taken!");
        saveUser(user);
        // Save initial period date to history 
        addPeriodDate(user.getUsername(), user.getLastPeriod());
    }

    public User authenticate(String u, String p) {
        Path path = getProfilePath(u);
        if (!Files.exists(path)) return null;
        try (BufferedReader r = Files.newBufferedReader(path)) {
            String line = r.readLine();
            if (line != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5 && parts[0].equals(u) && parts[1].equals(p)) {
                    return new User(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), LocalDate.parse(parts[4]));
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return null;
    }

    public void saveUser(User user) {
        try (BufferedWriter w = Files.newBufferedWriter(getProfilePath(user.getUsername()))) {
            w.write(String.format("%s|%s|%s|%d|%s", user.getUsername(), user.getPassword(), user.getEmail(), user.getCycleLength(), user.getLastPeriod()));
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    // Save Mood dgn level
    public void appendMood(MoodEntry m, String username) {
        try (BufferedWriter w = Files.newBufferedWriter(getMoodPath(username), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            int level = m.getMoodLevel(); 
            
            // Format: DATE|TYPE|NOTE|LEVEL
            w.write(m.getDate() + "|" + m.getType() + "|" + m.getNote().replace("\n", "\\n") + "|" + level);
            w.newLine();
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }

    // Load Mood dengan level
    public List<MoodEntry> loadMoods(String username) {
        List<MoodEntry> list = new ArrayList<>();
        Path p = getMoodPath(username);
        if (!Files.exists(p)) return list;
        
        try (BufferedReader r = Files.newBufferedReader(p)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split("\\|");
                
                // Minimal ada 2 bagian: Date dan Type
                if (parts.length >= 2) {
                    LocalDate date = LocalDate.parse(parts[0]);
                    MoodType type = MoodType.valueOf(parts[1]);
                    String note = parts.length > 2 ? parts[2].replace("\\n", "\n") : "";
                    
                    // Cek apakah ada bagian ke-4 (Level)
                    int level = 0;
                    if (parts.length > 3) {
                        try {
                            level = Integer.parseInt(parts[3]);
                        } catch (NumberFormatException e) {
                            level = 0;
                        }
                    }

                    // Polymorphism
                    // Jika ada level (>0), buat IntensityMoodEntry. Jika tidak, MoodEntry biasa.
                    if (level > 0) {
                        list.add(new IntensityMoodEntry(date, type, note, level));
                    } else {
                        list.add(new MoodEntry(date, type, note));
                    }
                }
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }
        return list;
    }

    // Period history
    public void addPeriodDate(String username, LocalDate date) {
        List<LocalDate> existing = loadPeriodDates(username);
        if (existing.contains(date)) return;
        try (BufferedWriter w = Files.newBufferedWriter(getPeriodPath(username), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(date.toString());
            w.newLine();
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }

    public List<LocalDate> loadPeriodDates(String username) {
        List<LocalDate> list = new ArrayList<>();
        Path p = getPeriodPath(username);
        if (!Files.exists(p)) return list;
        try (BufferedReader r = Files.newBufferedReader(p)) {
            String line;
            while ((line = r.readLine()) != null) {
                try { 
                    list.add(LocalDate.parse(line.trim())); 
                } catch (Exception e) {}
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }
}