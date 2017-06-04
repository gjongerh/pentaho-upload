package com.virtorg.test.jersey;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;

public class UploadJerseyTest extends JerseyTest {

    public UploadJerseyTest()throws Exception {
        super("com.virtorg.tst.jersey.component");
    }

    @Test
    public void pingTest() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("tst/ping").get(String.class);
        assertEquals("pong", responseMsg);
    }
}
