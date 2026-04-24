package com.smartcampus.resources;

import com.smartcampus.models.*;
import com.smartcampus.exceptions.CustomExceptions.SensorUnavailableException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SensorReadingResource {
    private String sensorId;
    private static final Map<String, List<SensorReading>> history = new HashMap<>();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getHistory() {
        return history.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public SensorReading add(SensorReading r) {
        Sensor s = SensorResource.sensorData.get(sensorId);

        if (s == null) {
            throw new WebApplicationException("Sensor not found", 404);
        }

        if ("MAINTENANCE".equalsIgnoreCase(s.getStatus())) {
            throw new SensorUnavailableException("Sensor is under maintenance.");
        }

        history.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(r);
        s.setCurrentValue(r.getValue());

        return r;
    }
}