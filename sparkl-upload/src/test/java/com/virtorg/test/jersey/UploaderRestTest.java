package com.virtorg.test.jersey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

/*
 * Author: Gerard Jongerhuis, g.jongerhuis@virtorg.nl
 */
public class UploaderRestTest extends JerseyTest {

    public UploaderRestTest()throws Exception {
        super("com.virtorg.bi.sparkl.ws");
    }

    @Override
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder("com.virtorg.bi.sparkl.ws")
            .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
            .build();
    } // configure()

    
    @Test
    public void pingTest() {
        WebResource webResource = resource();
        String responseMsg = webResource.path("plugin/api/upload/ping").get(String.class);
        assertEquals("pong", responseMsg);
    }

	/*
	 * Test upload file "work/eurotext.tif" to "../temp/eurotext.tif"
	 * and cleanup the uploaded file
	 * 
	 */
    @Test
	public void uploadTest() throws IOException, ParseException {
		WebResource webResource = resource();
		assertNotNull(webResource);

		FileDataBodyPart filePart = new FileDataBodyPart("file", new File("work/eurotext.tif"));
		assertNotNull(filePart);

		final MultiPart multiPartEntity = new MultiPart();
		multiPartEntity.bodyPart(new BodyPart()
			.entity("hello"))
			.bodyPart(filePart);
		assertNotNull(multiPartEntity);

		ClientResponse response = webResource.path("plugin/api/upload/drop")
			.type(MediaType.MULTIPART_FORM_DATA_TYPE)		// multiPartEntity.getMediaType() gives multipart/mixed
			.accept("application/json")
			.post(ClientResponse.class, multiPartEntity);

		multiPartEntity.close();

		assertEquals(200, response.getStatus());
		assertEquals("application/json", response.getType().toString());
		
		// Extract filename from json
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject)parser.parse(response.getEntity(String.class));
		String filename = (String) json.get("filename");
		System.out.println("Filename: "+filename);

		//JSONObject json2 = response.getEntity(JSONObject.class);
		
		File uploaded = new File(filename);
		assertTrue(uploaded.exists());
		
		//cleanup
		String folder = uploaded.getParent();
		uploaded.delete();
		new File(folder).delete();
	}
}
