package com.smartcampus.exceptions;

public class CustomExceptions {
    public static class RoomNotEmptyException extends RuntimeException {
        public RoomNotEmptyException(String m) { super(m); }
    }
    public static class LinkedResourceNotFoundException extends RuntimeException {
        public LinkedResourceNotFoundException(String m) { super(m); }
    }
    public static class SensorUnavailableException extends RuntimeException {
        public SensorUnavailableException(String m) { super(m); }
    }
}