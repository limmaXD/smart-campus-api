package com.smartcampus.resources;
import com.smartcampus.models.Sensor;
import com.smartcampus.exceptions.CustomExceptions.LinkedResourceNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
public class SensorResource {
    public static final Map<String, Sensor> sensorData = new ConcurrentHashMap<>();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(Sensor s) {
        // Validation: Does the room exist?
        if (!SensorRoomResource.roomData.containsKey(s.getRoomId())) {
            throw new LinkedResourceNotFoundException("Validation Failed: Room ID not found.");
        }
        sensorData.put(s.getId(), s);
        SensorRoomResource.roomData.get(s.getRoomId()).getSensorIds().add(s.getId());
        return Response.status(201).entity(s).build();
    }

    @GET
    public List<Sensor> get(@QueryParam("type") String type) {
        if (type == null) return new ArrayList<>(sensorData.values());
        return sensorData.values().stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .toList();
    }

    // Task 4: Sub-resource locator for readings
    @Path("/{sensorId}/read")
    public SensorReadingResource getReadings(@PathParam("sensorId") String id) {
        return new SensorReadingResource(id);
    }
}