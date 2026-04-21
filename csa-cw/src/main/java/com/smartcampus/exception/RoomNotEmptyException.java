package com.smartcampus.exception;

public class RoomNotEmptyException extends RuntimeException {

    public RoomNotEmptyException() {
        super();
    }

    public RoomNotEmptyException(String message) {
        super(message);
    }
}