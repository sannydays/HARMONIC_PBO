package com.app.hormontracker.model.mood;

import java.time.LocalDate;

public class MoodEntry {
    private LocalDate date;
    private MoodType type;
    private String note;

    public MoodEntry(LocalDate date, MoodType type, String note) {
        this.date = date;
        this.type = type;
        this.note = note;
    }

    public LocalDate getDate() { return date; }
    public MoodType getType() { return type; }
    public String getNote() { return note; }

    // TAMBAHAN PENTING: Method ini harus ada agar Controller bisa membacanya
    // Subclass (IntensityMoodEntry) akan meng-override method ini
    public int getMoodLevel() {
        return 0; 
    }
}