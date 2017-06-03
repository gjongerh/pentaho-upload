package com.virtorg.bi.tst.components;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Path("/")
@Component
public class HealthController {

    @Autowired private ConfigurableApplicationContext ctx;
    @Autowired private TaskScheduler taskScheduler;

    /*
     * ShutdownProgram runnable class
     */
    private class ShutdownProgram implements Runnable {
        public void run() {
            System.out.println("Starting shutdown the App...");
            ctx.close();
        }
    }

    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public String health() {
    	return "Jersey: Up and Running!";
    }

    @GET
    @Path("/shutdown")
    public String shutdown() {
    	Date date = new Date();
    	taskScheduler.scheduleWithFixedDelay(new ShutdownProgram(), date.getTime()+1000);	// start in 1 sec (1000ms)
    	return "Shutdown will be initiated over 1 sec...";
    }
}
