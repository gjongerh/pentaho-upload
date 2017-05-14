package com.virtorg.bi.tst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SparklUploadTestApp {

    private static SpringApplication app;
    private static ConfigurableApplicationContext ctx;
    
	@GetMapping("/")
    public String home() {
        return "Testing the Sparkl Upload App...";
    }

    @GetMapping("/shutdown")
    public void shutdown() {
    	//SpringApplication.exit(ctx, () -> 0);
    	ctx.close();
    }
    
    public static void main(String[] args) {
        //SpringApplication.run(SparklUploadTestApp.class, args);
        app = new SpringApplication(SparklUploadTestApp.class);
        ctx = app.run(args);
        System.out.print("done");
   }
}
