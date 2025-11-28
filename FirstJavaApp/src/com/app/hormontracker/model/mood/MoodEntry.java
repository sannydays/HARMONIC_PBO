package com.app.hormontracker.model.mood;

import java.time.LocalDate;

public class MoodEntry {
    private final LocalDate date;
    private final MoodType mood;
    private final String note;

    public MoodEntry(LocalDate date, MoodType mood, String note) {
        this.date = date;
        this.mood = mood;
        this.note = note;
    }

    public LocalDate getDate() { return date; }
    public MoodType getMood() { return mood; }
    public String getNote() { return note; }
}