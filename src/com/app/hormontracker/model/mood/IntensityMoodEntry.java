package com.app.hormontracker.model.mood;

import java.time.LocalDate;

public class IntensityMoodEntry extends MoodEntry {
    private final int intensity;

    public IntensityMoodEntry(LocalDate date, MoodType type, String note, int intensity) {
        super(date, type, note); // Memanggil parent constructor
        this.intensity = intensity;
    }

    @Override
    public int getMoodLevel() { // Setter untuk mengakses intensitas mood
        return intensity;
    }
}