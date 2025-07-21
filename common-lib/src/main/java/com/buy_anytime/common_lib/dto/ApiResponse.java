package com.buy_anytime.common_lib.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private T data;
    private int statusCode;

    // Default constructor
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * THIS IS THE NEW, CORRECT CONSTRUCTOR that all other services need.
     * It accepts a timestamp, any type of data (T), and a status code.
     */
    public ApiResponse(LocalDateTime timestamp, T data, int statusCode) {
        this.timestamp = timestamp;
        this.data = data;
        this.statusCode = statusCode;
    }

    /**
     * This is a specific constructor for handling validation errors (a Map).
     * The logic has been corrected.
     */
    public ApiResponse(LocalDateTime timestamp, Map<String, String> errors, int statusCode) {
        this.timestamp = timestamp;
        this.data = (T) errors; // Cast the map to the generic type T
        this.statusCode = statusCode;
    }
    // --- Getters and Setters ---

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
