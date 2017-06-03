package com.virtorg.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.virtorg.bi.tst.JerseyConfig;

/*
 * inspired by https://geowarin.github.io/a-simple-spring-boot-and-jersey-application.html
 * Author: Gerard Jongerhuis
 * 
 */
public class UploadJerseyTest extends JerseyTest {

    @Override
    protected Application configure() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        return new JerseyConfig().property("contextConfig", context);
    }

    @Test
    public void testHello() {
        final String hello = target("upload/hello").request().get(String.class);
        assertThat(hello).isEqualTo("Hello World");
    }
}
