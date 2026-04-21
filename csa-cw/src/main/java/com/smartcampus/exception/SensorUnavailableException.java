package com.smartcampus.exception;

public class SensorUnavailableException extends RuntimeException {

    public SensorUnavailableException() {
        super();
    }

    public SensorUnavailableException(String message) {
        super(message);
    }
}