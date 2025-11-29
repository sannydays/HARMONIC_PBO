package com.app.hormontracker.model.cycle;
import java.time.LocalDate;
public class CycleRecord {
    private LocalDate start;

    public CycleRecord(LocalDate start) { // Setter
        this.start = start; 
    }

    public LocalDate getStart() { 
        return start; 
    }
}