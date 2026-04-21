package com.smartcampus.filter;


import javax.ws.rs.container.*;
import javax.ws.rs.ext.*;
import java.util.*;
import java.util.logging.*;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger logger = Logger.getLogger("API");

    @Override
    public void filter(ContainerRequestContext request) {
        logger.info("Request: " + request.getMethod() + " " + request.getUriInfo().getPath());
    }

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {
        logger.info("Response Status: " + res.getStatus());
    }
}