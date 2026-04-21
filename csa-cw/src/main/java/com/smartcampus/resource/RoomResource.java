package com.smartcampus.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.Room;
import com.smartcampus.model.SensorReading;
import com.smartcampus.storage.DataStore;


@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class RoomResource {

    @GET
    public Collection<Room> getRooms() {
        return DataStore.rooms.values();
    }

    @POST
    public Response addRoom(Room room) {
        DataStore.rooms.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    @GET
    @Path("/{id}")
    public Room getRoom(@PathParam("id") String id) {
        return DataStore.rooms.get(id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {

        Room room = DataStore.rooms.get(id);

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room has sensors");
        }

        DataStore.rooms.remove(id);
        return Response.ok().build();
    }
}