package com.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Map;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response getInfo() {
        return Response.ok(
                Map.of(
                        "version", "v1",
                        "rooms", "/api/v1/rooms",
                        "sensors", "/api/v1/sensors"
                )
        ).build();
    }
}