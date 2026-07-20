package com.bulb;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

public class ApiServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/simulate", new SimulateHandler());
        
        // Add CORS headers handler wrapper
        server.createContext("/", exchange -> {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String response = "Server is running.";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Java Backend API Server started on http://localhost:" + port);
    }

    static class SimulateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json");

            String query = exchange.getRequestURI().getQuery();
            String dateParam = getQueryParam(query, "date");

            if (dateParam == null) {
                sendError(exchange, 400, "Missing 'date' parameter. Use format yyyy-MM-dd.");
                return;
            }

            try {
                LocalDate targetDate = LocalDate.parse(dateParam);
                SimulationEngine engine = new SimulationEngine(targetDate.getYear());
                SimulationResult result = engine.runSimulation(targetDate);
                
                String jsonResponse = buildJsonResponse(targetDate, result);
                
                exchange.sendResponseHeaders(200, jsonResponse.length());
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();

            } catch (DateTimeParseException e) {
                sendError(exchange, 400, "Invalid date format. Use yyyy-MM-dd.");
            } catch (Exception e) {
                sendError(exchange, 500, "Server Error: " + e.getMessage());
            }
        }

        private String getQueryParam(String query, String key) {
            if (query == null) return null;
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length > 1 && pair[0].equals(key)) {
                    return pair[1];
                }
            }
            return null;
        }

        private void sendError(HttpExchange exchange, int code, String msg) throws IOException {
            String json = "{\"error\": \"" + msg + "\"}";
            exchange.sendResponseHeaders(code, json.length());
            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes());
            os.close();
        }

        private String buildJsonResponse(LocalDate targetDate, SimulationResult result) {
            Operation op = result.getOperation();
            ToggleEngine toggleEngine = result.getToggleEngine();

            String holiday = op.getHoliday() != null ? op.getHoliday().getName() : "None";
            String scheduledOp = op.getScheduledType().toString();
            String executedOp = op.getExecutedType() != null ? op.getExecutedType().toString() : "None";
            
            String reason = "Regular schedule";
            if (op.getStatus() == ExecutionStatus.HOLIDAY) {
                reason = "Holiday";
            } else if (op.getStatus() == ExecutionStatus.DISPLACED) {
                reason = "Displaced from previous holiday";
            }

            String bulbsJson = toggleEngine.getBulbs().stream()
                .map(b -> "{\"id\": " + b.getId() + ", \"state\": \"" + b.getState() + "\"}")
                .collect(Collectors.joining(","));

            return String.format(
                "{\"date\": \"%s\", \"holiday\": \"%s\", \"scheduled\": \"Operation %s\", \"executed\": \"%s\", \"reason\": \"%s\", \"bulbs\": [%s]}",
                targetDate.toString(), holiday, scheduledOp, executedOp, reason, bulbsJson
            );
        }
    }
}
