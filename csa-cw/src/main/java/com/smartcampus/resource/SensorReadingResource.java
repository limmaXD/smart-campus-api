package com.smartcampus.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.Room;
import com.smartcampus.model.SensorReading;
import com.smartcampus.storage.DataStore;

public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getAll() {
        return DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
    }

    @POST
    public Response add(SensorReading reading) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor.getStatus().equals("MAINTENANCE")) {
            throw new SensorUnavailableException("Sensor under maintenance");
        }

        if (sensor == null) {
            throw new LinkedResourceNotFoundException("Sensor not found");
        }

        DataStore.readings
                .computeIfAbsent(sensorId, k -> new ArrayList<>())
                .add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(201).entity(reading).build();
    }
}