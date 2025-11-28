package com.app.hormontracker.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LogicService {
    public enum Phase { MENSTRUATION, FOLLICULAR, OVULATION, LUTEAL }

    public static Phase predictPhase(LocalDate lastPeriod, int cycleLen, LocalDate today) {
        long daysSince = ChronoUnit.DAYS.between(lastPeriod, today);
        long dayInCycle = ((daysSince % cycleLen) + cycleLen) % cycleLen + 1; // Handle negative dates properly

        if (dayInCycle <= 5) return Phase.MENSTRUATION;
        if (dayInCycle <= 13) return Phase.FOLLICULAR;
        if (dayInCycle == 14) return Phase.OVULATION;
        return Phase.LUTEAL;
    }

    public static long getDayInCycle(LocalDate lastPeriod, int cycleLen) {
        long daysSince = ChronoUnit.DAYS.between(lastPeriod, LocalDate.now());
        return ((daysSince % cycleLen) + cycleLen) % cycleLen + 1;
    }
}