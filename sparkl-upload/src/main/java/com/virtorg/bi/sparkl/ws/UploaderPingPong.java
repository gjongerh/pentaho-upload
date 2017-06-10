package com.virtorg.bi.sparkl.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/*
 * UploaderPingPong; very simple service to test if the server is working
 * Author: Gerard Jongerhuis, g.jongerhuis@virtorg.nl
 */

@Path("/{plugin}/api/ping")
@Produces({ MediaType.APPLICATION_JSON })
public class UploaderPingPong {
	
	@GET
	@Path("/")
	public String pingPong() {
		return "[\"pong\"]";
	}
}