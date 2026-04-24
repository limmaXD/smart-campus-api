package com.smartcampus.exceptions;

import com.smartcampus.exceptions.CustomExceptions;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.*;
import java.util.Map;

public class ExceptionMappers {

    @Provider
    public static class RoomMapper implements ExceptionMapper<CustomExceptions.RoomNotEmptyException> {
        public Response toResponse(CustomExceptions.RoomNotEmptyException e) {
            return Response.status(409).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @Provider
    public static class LinkedMapper implements ExceptionMapper<CustomExceptions.LinkedResourceNotFoundException> {
        public Response toResponse(CustomExceptions.LinkedResourceNotFoundException e) {
            return Response.status(422).entity(Map.of("error", e.getMessage())).build();
        }
    }

    // ADD THIS ONE: This fixes the 403 Forbidden requirement
    @Provider
    public static class SensorUnavailableMapper implements ExceptionMapper<CustomExceptions.SensorUnavailableException> {
        public Response toResponse(CustomExceptions.SensorUnavailableException e) {
            return Response.status(403).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @Provider
    public static class GlobalMapper implements ExceptionMapper<Throwable> {
        public Response toResponse(Throwable t) {
            t.printStackTrace();
            return Response.status(500).entity(Map.of("error", "An internal error occurred.")).build();
        }
    }
}