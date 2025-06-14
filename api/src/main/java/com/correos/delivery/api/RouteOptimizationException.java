package com.correos.delivery.api;

public class RouteOptimizationException extends Exception {
    public RouteOptimizationException(String message) {
        super(message);
    }
    public RouteOptimizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
