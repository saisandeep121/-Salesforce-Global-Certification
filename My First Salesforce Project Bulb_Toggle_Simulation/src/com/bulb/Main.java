package com.bulb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Date (yyyy-MM-dd):");
        String input = scanner.nextLine();

        try {
            LocalDate targetDate = LocalDate.parse(input);
            int year = targetDate.getYear();

            SimulationEngine engine = new SimulationEngine(year);
            SimulationResult result = engine.runSimulation(targetDate);
            
            Operation op = result.getOperation();
            ToggleEngine toggleEngine = result.getToggleEngine();

            System.out.println("----------------------------------------");
            System.out.println("Date:\n" + targetDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
            
            System.out.println("\nHoliday:");
            if (op.getHoliday() != null) {
                System.out.println(op.getHoliday().getName());
            } else {
                System.out.println("No");
            }

            System.out.println("\nScheduled Operation:\nOperation " + op.getScheduledType());

            System.out.println("\nExecuted:");
            if (op.getExecutedType() != null) {
                System.out.println("Yes (" + op.getExecutedType() + ")");
            } else {
                System.out.println("No");
            }

            System.out.println("\nReason:");
            if (op.getStatus() == ExecutionStatus.HOLIDAY) {
                System.out.println("Holiday");
            } else if (op.getStatus() == ExecutionStatus.DISPLACED) {
                System.out.println("Displaced from previous holiday");
            } else {
                System.out.println("Regular schedule");
            }

            System.out.println("\nBulbs Toggled:");
            if (op.getExecutedType() == null) {
                System.out.println("None");
            } else if (op.getExecutedType() == OperationType.A) {
                System.out.println("1, 3, 5, 7");
            } else {
                System.out.println("2, 4, 6");
            }

            System.out.println("\nCurrent Bulb States:");
            for (Bulb b : toggleEngine.getBulbs()) {
                System.out.println("Bulb " + b.getId() + " : " + b.getState());
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}
