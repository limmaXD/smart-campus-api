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
        if (r != null && !r.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Constraint Violation: Room contains sensors.");
        }
        roomData.remove(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        Room r = roomData.get(id);
        if (r == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found")).build();
        }
        return Response.ok(r).build();
    }
}
