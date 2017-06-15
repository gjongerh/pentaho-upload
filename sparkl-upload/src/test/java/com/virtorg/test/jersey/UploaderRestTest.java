package com.virtorg.test.jersey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.virtorg.bi.sparkl.dto.FileReceived;
import com.virtorg.bi.sparkl.dto.SendResponse;
import com.virtorg.bi.sparkl.ws.UploaderDropFile;
import com.virtorg.bi.sparkl.ws.UploaderRedirFile;
import com.virtorg.bi.sparkl.ws.UploaderSendFile;

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
        String responseMsg = webResource.path("plugin/api/ping").get(String.class);
        assertEquals("[\"pong\"]", responseMsg);
    }

    @Test
    public void parameterDropTest() {
    	UploaderDropFile component = new UploaderDropFile();
    	assertNotNull(component);
    	
    	component.setFolder("folder");
    	assertEquals("folder", component.getFolder());
    }
    
    /*
	 * Test upload file "work/eurotext.tif"
	 * and cleanup the uploaded file
	 * 
	 */
    @Test
	public void uploadDropTest() throws IOException, ParseException {
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

		File uploaded = new File(filename);
		assertTrue(uploaded.exists());
		
		//cleanup
		String folder = uploaded.getParent();
		uploaded.delete();
		new File(folder).delete();
	}

    @Test
    public void parameterRedirTest() {
    	UploaderRedirFile component = new UploaderRedirFile();
    	assertNotNull(component);
    	
    	component.setEndpoint("plugin/test/endpoint");
    	assertEquals("plugin/test/endpoint", component.getEndpoint());
    }
    
    @Test
	public void uploadRedirTest() throws IOException, ParseException {
    	WebResource webResource = resource();
		assertNotNull(webResource);
		webResource.setProperty(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, false);

		FileDataBodyPart filePart = new FileDataBodyPart("file", new File("work/eurotext.tif"));
		assertNotNull(filePart);

		FormDataMultiPart multiPartEntity = new FormDataMultiPart();
		multiPartEntity
			.field("queryParameters", "&paramtemplate=tapa-default", MediaType.TEXT_PLAIN_TYPE)
			.bodyPart(filePart);
		assertNotNull(multiPartEntity);

		ClientResponse response = webResource.path("plugin/api/upload/redir")
			.type(multiPartEntity.getMediaType())
			.accept("application/json")
			.post(ClientResponse.class, multiPartEntity);

		multiPartEntity.close();

		assertEquals(307, response.getStatus());
		System.out.println(response.getLocation());
	}

    @Test
    public void parameterSendTest() {
    	UploaderSendFile component = new UploaderSendFile();
    	assertNotNull(component);
    	
    	component.setEndpoint("plugin/test/endpoint");
    	assertEquals("plugin/test/endpoint", component.getEndpoint());
    	
    	component.setUser("admin01");
    	assertEquals("admin01", component.getUser());
    	
    	component.setPassword("passWord01");
    	assertEquals("passWord01", component.getPassword());
    }
    
    /*
     * endpoint plugin/api/upload/send has by default a reference to the PingPong testing endpoint
     * so for testing we get the result of ["pong"] 
     */
    @Test
	public void uploadSendTest() throws IOException, ParseException {
    	WebResource webResource = resource();
		assertNotNull(webResource);
		webResource.setProperty(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, false);

		FileDataBodyPart filePart = new FileDataBodyPart("file", new File("work/eurotext.tif"));
		assertNotNull(filePart);

		FormDataMultiPart temp = new FormDataMultiPart();
		temp.close();
		
		FormDataMultiPart multiPartEntity = new FormDataMultiPart();
		multiPartEntity
			.field("queryParameters", "&paramtemplate=tapa-default", MediaType.TEXT_PLAIN_TYPE)
			.bodyPart(filePart);
		assertNotNull(multiPartEntity);

		ClientResponse response = webResource.path("plugin/api/upload/send")
			.type(multiPartEntity.getMediaType())
			.accept(MediaType.APPLICATION_JSON)
			.post(ClientResponse.class, multiPartEntity);

		multiPartEntity.close();

		assertEquals(200, response.getStatus());
		assertEquals("application/json", response.getType().toString());
		
		// Extract filename from json
		JSONParser parser = new JSONParser();
		String result = response.getEntity(String.class);
		JSONArray myTemp = (JSONArray)parser.parse(result);
		assertEquals( 1, myTemp.size());
		assertEquals( "pong", myTemp.get(0));
	}
    
    @Test
	public void uploadSendJsonTest() throws IOException, ParseException {
    	WebResource webResource = resource();
		assertNotNull(webResource);
		webResource.setProperty(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, false);

		FileReceived sendfile = new FileReceived();
		sendfile.setFilename("magweg01.txt");
		//sendfile.setData(new JSONObject());
		
		ObjectMapper mapper = new ObjectMapper();		// POJO to JSON
		
		ClientResponse response = webResource.path("plugin/api/upload/send")
			.type(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.post(ClientResponse.class, mapper.writeValueAsString(sendfile));


		assertEquals(200, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON, response.getType().toString());
		
		// Extract filename from json
		SendResponse result = mapper.readValue(response.getEntity(String.class), SendResponse.class);
		assertTrue( result.getTimestamp().length() > 4);
	}
}
