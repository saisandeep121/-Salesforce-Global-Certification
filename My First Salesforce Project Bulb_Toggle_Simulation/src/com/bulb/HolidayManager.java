package com.bulb;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class HolidayManager {
    private final Map<LocalDate, Holiday> holidays;

    public HolidayManager(int year) {
        this.holidays = new HashMap<>();
        loadHolidays(year);
    }

    private void loadHolidays(int year) {
        addHoliday(LocalDate.of(year, 1, 26), "Republic Day");
        addHoliday(LocalDate.of(year, 3, 3), "Holi");
        addHoliday(LocalDate.of(year, 3, 20), "Eid-ul-Fitr");
        addHoliday(LocalDate.of(year, 4, 3), "Good Friday");
        addHoliday(LocalDate.of(year, 8, 15), "Independence Day");
        addHoliday(LocalDate.of(year, 10, 2), "Gandhi Jayanti");
        addHoliday(LocalDate.of(year, 10, 19), "Dussehra");
        addHoliday(LocalDate.of(year, 11, 8), "Diwali");
        addHoliday(LocalDate.of(year, 12, 25), "Christmas");
    }

    private void addHoliday(LocalDate date, String name) {
        holidays.put(date, new Holiday(date, name));
    }

    public boolean isHoliday(LocalDate date) {
        return holidays.containsKey(date);
    }

    public Holiday getHoliday(LocalDate date) {
        return holidays.get(date);
    }
}
