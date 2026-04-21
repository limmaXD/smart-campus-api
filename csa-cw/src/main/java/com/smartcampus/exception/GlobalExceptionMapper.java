package com.smartcampus.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        ex.printStackTrace(); // 🔥 ADD THIS

        return Response.status(500)
                .entity(Map.of("error", "Internal Server Error"))
                .build();
    }
}