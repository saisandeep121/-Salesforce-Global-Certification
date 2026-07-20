package com.bulb;

import java.time.LocalDate;

public class Operation {
    private final LocalDate date;
    private final OperationType scheduledType;
    private final OperationType executedType;
    private final ExecutionStatus status;
    private final Holiday holiday;

    public Operation(LocalDate date, OperationType scheduledType, OperationType executedType, ExecutionStatus status, Holiday holiday) {
        this.date = date;
        this.scheduledType = scheduledType;
        this.executedType = executedType;
        this.status = status;
        this.holiday = holiday;
    }

    public LocalDate getDate() { return date; }
    public OperationType getScheduledType() { return scheduledType; }
    public OperationType getExecutedType() { return executedType; }
    public ExecutionStatus getStatus() { return status; }
    public Holiday getHoliday() { return holiday; }
}
