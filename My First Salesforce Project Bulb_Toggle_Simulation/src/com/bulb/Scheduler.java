package com.bulb;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;

public class Scheduler {
    private final HolidayManager holidayManager;
    private final Deque<OperationType> pendingOperations;

    public Scheduler(HolidayManager holidayManager) {
        this.holidayManager = holidayManager;
        this.pendingOperations = new ArrayDeque<>();
    }

    public Operation calculateOperationForDate(LocalDate date) {
        // Base cycle: Jan 1 is A. Jan 2 is B. Jan 3 is A...
        // Day of year is 1-indexed. So day 1 is A (1 % 2 = 1, A). Day 2 is B (2 % 2 = 0, B).
        int dayOfYear = date.getDayOfYear();
        OperationType scheduledType = (dayOfYear % 2 != 0) ? OperationType.A : OperationType.B;

        pendingOperations.addLast(scheduledType);

        if (holidayManager.isHoliday(date)) {
            Holiday holiday = holidayManager.getHoliday(date);
            return new Operation(date, scheduledType, null, ExecutionStatus.HOLIDAY, holiday);
        } else {
            OperationType executedType = pendingOperations.pollFirst();
            ExecutionStatus status = (executedType == scheduledType && pendingOperations.isEmpty()) 
                                     ? ExecutionStatus.REGULAR : ExecutionStatus.DISPLACED;
            return new Operation(date, scheduledType, executedType, status, null);
        }
    }
}
