package com.virtorg.bi.tst;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;


/*
 * Test Server working with couple of tests:
 * - javax.ws.rs.GET
 * - org.springframework.web.bind.annotation.RestController		#NOTE: has and own URL mapping does not use the servlet offset
 * 
 * Author: Gerard Jongerhuis
 */

@SpringBootApplication
public class SparklUploadTestApp {

    public static void main(String[] args) {
        SpringApplication.run(SparklUploadTestApp.class, args);
        System.out.print("done");
   }

    @Bean
    public ServletRegistrationBean jerseyServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/jersey/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registration;
    }
}
