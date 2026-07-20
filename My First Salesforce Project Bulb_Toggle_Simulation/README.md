# Day-1 : First Salesforce Project [Bulb Toggle Simulation]

## Overview
This project is a production-quality simulation of a 7-bulb lighting system driven by a repeating A/B schedule, governed by Indian Gazetted Holiday displacement logic. It features a robust **Java 17 backend** and a modern **React + Vite frontend** with premium UI animations.

This repository serves as **Day-1** of the Salesforce Global Certification journey, demonstrating algorithmic problem-solving, full-stack architecture, and clean code practices.

## Project Structure
```text
Day-1 : First Salesforce Project [Bulb Toggle Simulation]/
│
├── src/                  # Java Backend Core Logic
│   └── com/bulb/         
│       ├── Main.java               # Console-based simulation entry point
│       ├── ApiServer.java          # HTTP API Server (Native Java, port 8080)
│       ├── SimulationEngine.java   # Date-based state calculation & replaying
│       ├── ToggleEngine.java       # Odd/Even series state enforcement
│       ├── Scheduler.java          # A/B scheduling & queue management
│       ├── HolidayManager.java     # Indian Gazetted Holiday definitions
│       └── ... (Domain Models)
│
└── frontend/             # React + Vite UI
    ├── src/
    │   ├── App.jsx       # Main Dashboard UI & API Integration
    │   ├── App.css       # Glassmorphism, animations, & styling
    │   └── main.jsx      # React DOM entry point
    └── package.json      # Frontend dependencies
```

## The Logic Explained

### 1. Structural Integrity (Odd/Even Series)
At all times, the system guarantees that the 7 bulbs are never completely ON or completely OFF (except on holidays). The `ToggleEngine` strictly enforces two valid states:
- **Odd Series (Default & Operation A):** Bulbs 1, 3, 5, 7 are ON. Bulbs 2, 4, 6 are OFF.
- **Even Series (Operation B):** Bulbs 2, 4, 6 are ON. Bulbs 1, 3, 5, 7 are OFF.

### 2. A/B Schedule Displacement Algorithm
Starting from January 1st, operations alternate between `Operation A` and `Operation B` every single day.
However, if a day falls on an **Indian Gazetted Holiday**:
- The scheduled operation is **not skipped**; it is placed into a displacement queue (`Deque`).
- All bulbs are turned entirely **OFF** for the duration of the holiday.
- On the next available working day, the displaced operation cascades and executes, pushing the rest of the schedule back. This guarantees that maintenance or load-balancing rotations are never missed.

## How to Run

### 1. Start the Java Backend API
The backend requires no external dependencies (like Spring Boot or Maven). It uses Java's native `HttpServer`.
```bash
cd src
javac com/bulb/*.java
java com.bulb.ApiServer
```
*The server will start on `http://localhost:8080`.*

### 2. Start the React Frontend
Open a new terminal window and run:
```bash
cd frontend
npm install
npm run dev
```
*Open `http://localhost:5173` in your browser to view the interactive simulation.*
