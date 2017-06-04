package com.virtorg.test.jersey;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;

public class UploaderRestTest extends JerseyTest {

    public UploaderRestTest()throws Exception {
        super("com.virtorg.bi.sparkl.ws");
    }

    @Test
    public void pingTest() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("plugin/api/upload/ping").get(String.class);
        assertEquals("pong", responseMsg);
    }
}
