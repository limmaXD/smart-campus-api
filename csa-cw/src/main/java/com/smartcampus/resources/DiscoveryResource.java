package com.smartcampus.resources;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Path("/")
public class DiscoveryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> discover() {
        return Map.of(
                "apiVersion", "1.0",
                "contact", "lead.architect@university.edu",
                "resources", Map.of("rooms", "/api/v1/rooms", "sensors", "/api/v1/sensors")
        );
    }
}