package com.virtorg.test.jersey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.test.framework.JerseyTest;

public class UploadJerseyTest extends JerseyTest {

    public UploadJerseyTest()throws Exception {
        super("com.virtorg.tst.jersey.component");
        //this.configure().getClientConfig().getProperties();
        
    }

//    @Override
//    public Map<String, Object> getProperties() {
//        Map<String, Object> props = new HashMap<>();
//        props.put("jersey.config.server.provider.classnames", 
//                "org.glassfish.jersey.media.multipart.MultiPartFeature");
//        return props;
//    }
    
    @Test
    public void pingTest() {
        WebResource webResource = resource();
        assertNotNull(webResource);
        String responseMsg = webResource.path("tst/ping").get(String.class);
        assertEquals("pong", responseMsg);
    }

    @Test
    public void uploadTest() throws IOException {
        WebResource webResource = resource();
        assertNotNull(webResource);
        
        FileDataBodyPart filePart = new FileDataBodyPart("file", new File("work/eurotext.tif"));
        assertNotNull(filePart);
        
        final MultiPart multiPartEntity = new MultiPart();
        multiPartEntity.bodyPart(new BodyPart()
        	.entity("hello"))
        	.bodyPart(filePart);
//                .bodyPart(new BodyPart(new JaxbBean("xml"), MediaType.APPLICATION_XML_TYPE))
//                .bodyPart(new BodyPart(new JaxbBean("json"), MediaType.APPLICATION_JSON_TYPE));
        assertNotNull(multiPartEntity);
        
        ClientResponse response = webResource.path("tst/upload")
        	.type(multiPartEntity.getMediaType())			// MediaType.MULTIPART_FORM_DATA_TYPE        
        	.post(ClientResponse.class, multiPartEntity);	
        
        multiPartEntity.close();
        
        assertEquals(200, response.getStatus());
    }
}
