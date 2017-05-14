package com.virtorg.bi.tst;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SparklUploadTestApp {

    private static SpringApplication app;
    private static ConfigurableApplicationContext ctx;
    @Autowired
    private TaskScheduler taskScheduler;
    
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
    @GetMapping("/")
    public String home() {
        return "Testing the Sparkl Upload App...";
    }

    @GetMapping("/shutdown")
    public String shutdown() {
    	Date date = new Date();
    	taskScheduler.scheduleWithFixedDelay(new ShutdownProgram(), date.getTime()+1000);	// start in 1 sec (1000ms)
    	return "Shutdown will be initiated over 1 sec...";
    }
    
    public static void main(String[] args) {
        //SpringApplication.run(SparklUploadTestApp.class, args);
        app = new SpringApplication(SparklUploadTestApp.class);
        ctx = app.run(args);
        System.out.print("done");
   }
}
