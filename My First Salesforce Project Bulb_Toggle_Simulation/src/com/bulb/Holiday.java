package com.bulb;

import java.time.LocalDate;

public class Holiday {
    private final LocalDate date;
    private final String name;

    public Holiday(LocalDate date, String name) {
        this.date = date;
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}
