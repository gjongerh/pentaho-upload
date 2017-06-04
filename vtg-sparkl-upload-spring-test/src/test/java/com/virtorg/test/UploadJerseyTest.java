package com.virtorg.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
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

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);
    }
    
    @Test
    public void testHello() {
        final String hello = target("upload/hello").request().get(String.class);
        assertThat(hello).isEqualTo("Hello World");
    }

    @Test
    public void testUpload() throws IOException {
    	final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("./work/eurotext.tif"));
    	final FormDataMultiPart multipart = new FormDataMultiPart();
    	multipart.field("endpointPath", "plugin/proef");
    	multipart.bodyPart(filePart);
    	
    	final Response response = target("upload/file").request().post(Entity.entity(multipart, multipart.getMediaType()));
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo("File upload succesfull...");
        
        multipart.close();
    }
}
