package com.smartcampus.resources;
import com.smartcampus.models.Room;
import com.smartcampus.exceptions.CustomExceptions.RoomNotEmptyException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {
    // In-memory database (Task requirement: NO SQL)
    public static final Map<String, Room> roomData = new ConcurrentHashMap<>();

    @GET
    public List<Room> getAll() { return new ArrayList<>(roomData.values()); }

    @POST
    public Response create(Room room) {
        roomData.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Room r = roomData.get(id);
        // Safety Logic: Cannot delete if sensors are linked
        if (r != null && !r.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Constraint Violation: Room contains sensors.");
        }
        roomData.remove(id);
        return Response.noContent().build();
    }
}