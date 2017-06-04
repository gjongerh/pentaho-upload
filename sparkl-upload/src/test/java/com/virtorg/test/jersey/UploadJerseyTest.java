package com.virtorg.test.jersey;

import javax.ws.rs.core.Application;

import org.junit.Test;

import com.sun.jersey.test.framework.JerseyTest;

public class UploadJerseyTest extends JerseyTest {
	
    @Override
    protected Application configure() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        return new JerseyConfig()
                .property("contextConfig", context);
    }

    @Test
    public void testHello() {
        final String hello = target("hello").request().get(String.class);
        assertThat(hello).isEqualTo("Hello World");
    }
}
