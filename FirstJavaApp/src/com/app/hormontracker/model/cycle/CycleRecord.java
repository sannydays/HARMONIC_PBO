package com.app.hormontracker.model.cycle;

import com.app.hormontracker.model.base.BaseModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CycleRecord extends BaseModel {
    private final LocalDate startDate;
    private final int durationDays;
    private final String note;

    public CycleRecord(LocalDate startDate, int durationDays, String note) {
        super();
        this.startDate = startDate;
        this.durationDays = durationDays;
        this.note = note;
    }

    public LocalDate getStartDate() { return startDate; }
    public int getDurationDays() { return durationDays; }
    public String getNote() { return note; }

    public String toLine() {
        return startDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + "|" + durationDays + "|" + (note==null? "" : note.replace("\n","\\n"));
    }

    public static CycleRecord fromLine(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            java.time.LocalDate d = java.time.LocalDate.parse(parts[0]);
            int dur = Integer.parseInt(parts[1]);
            String note = parts.length >= 3 ? parts[2].replace("\\n", "\n") : "";
            return new CycleRecord(d, dur, note);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "CycleRecord{start=" + startDate + ", dur=" + durationDays + "}";
    }
}