package com.smartcampus.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;
import java.util.Map;

@Provider
public class RoomExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    public Response toResponse(RoomNotEmptyException ex) {
        return Response.status(409)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}