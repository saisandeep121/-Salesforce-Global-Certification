package com.bulb;

import java.time.LocalDate;

public class SimulationEngine {
    private final int year;
    private final HolidayManager holidayManager;

    public SimulationEngine(int year) {
        this.year = year;
        this.holidayManager = new HolidayManager(year);
    }

    public SimulationResult runSimulation(LocalDate targetDate) {
        if (targetDate.getYear() != year) {
            throw new IllegalArgumentException("Target date must be in the year " + year);
        }

        Scheduler scheduler = new Scheduler(holidayManager);
        ToggleEngine toggleEngine = new ToggleEngine();
        
        Operation finalOperation = null;

        LocalDate currentDate = LocalDate.of(year, 1, 1);
        while (!currentDate.isAfter(targetDate)) {
            Operation op = scheduler.calculateOperationForDate(currentDate);
            if (op.getExecutedType() != null) {
                toggleEngine.applyOperation(op.getExecutedType());
            } else if (op.getStatus() == ExecutionStatus.HOLIDAY) {
                toggleEngine.turnAllOff();
            }
            if (currentDate.equals(targetDate)) {
                finalOperation = op;
            }
            currentDate = currentDate.plusDays(1);
        }

        return new SimulationResult(finalOperation, toggleEngine);
    }
}
