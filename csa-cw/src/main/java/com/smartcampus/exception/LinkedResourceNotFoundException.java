package com.smartcampus.exception;

public class LinkedResourceNotFoundException extends RuntimeException {

    public LinkedResourceNotFoundException() {
        super();
    }

    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}