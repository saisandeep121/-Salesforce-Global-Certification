package com.bulb;

public class SimulationResult {
    private final Operation operation;
    private final ToggleEngine toggleEngine;

    public SimulationResult(Operation operation, ToggleEngine toggleEngine) {
        this.operation = operation;
        this.toggleEngine = toggleEngine;
    }

    public Operation getOperation() { return operation; }
    public ToggleEngine getToggleEngine() { return toggleEngine; }
}
