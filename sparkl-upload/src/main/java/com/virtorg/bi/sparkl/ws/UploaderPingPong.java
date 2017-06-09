package com.virtorg.bi.sparkl.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/*
 * UploaderPingPong; very simple service to test if the server is working
 * Author: Gerard Jongerhuis, g.jongerhuis@virtorg.nl
 */

@Path("/{plugin}/api/ping")
public class UploaderPingPong {
	
	@GET
	@Path("/")
	public String pingPong() {
		return "pong";
	}
}