package com.smartcampus;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        final ResourceConfig rc = new ResourceConfig().packages("com.smartcampus");
        final String baseUri = "http://localhost:8080/api/v1/";

        GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc);
        System.out.println("Smart Campus API started at " + baseUri);
    }
}