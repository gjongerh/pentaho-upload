package com.virtorg.bi.tst.components;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest")
public class MyController {

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

    /*
     * Servlet mappings
     */
    @GetMapping("/home")
    public String home() {
        return "Testing the Sparkl Upload App...";
    }

    @GetMapping("/shutdown")
    public String shutdown() {
    	Date date = new Date();
    	taskScheduler.scheduleWithFixedDelay(new ShutdownProgram(), date.getTime()+1000);	// start in 1 sec (1000ms)
    	return "Shutdown will be initiated over 1 sec...";
    }
    
}
