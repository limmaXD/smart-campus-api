package com.smartcampus.exceptions;

import com.smartcampus.exceptions.CustomExceptions;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.*;
import java.util.Map;

@Provider
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

    @Provider
    public static class GlobalMapper implements ExceptionMapper<Throwable> {
        public Response toResponse(Throwable t) {
            t.printStackTrace(); // Log for server console
            return Response.status(500).entity(Map.of("error", "An internal error occurred.")).build();
        }
    }
}