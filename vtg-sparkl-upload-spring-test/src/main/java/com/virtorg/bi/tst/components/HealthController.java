package com.virtorg.bi.tst.components;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Path("/")
@Component
public class HealthController {

    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public String health() {
    	return "Jersey: Up and Running!";
    }
}
