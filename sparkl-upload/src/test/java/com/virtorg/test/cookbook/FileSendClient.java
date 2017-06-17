package com.virtorg.test.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.Base64;
import com.virtorg.bi.sparkl.dto.FileReceived;

public class FileSendClient {

	@Test
	public void postClient() {
		try {

	        String user = "admin";
	        String password = "password";
	        
			FileReceived sendfile = new FileReceived();
			sendfile.setFilename("magweg01.txt");
			sendfile.setFile(Base64.encode("This is a test for sending a file base64 encoded..."));
			
			JSONObject data = new JSONObject();
			data.put( "paramTester", "proef");
			data.put( "paramproef", "test");
			sendfile.setData(data);
			
			ObjectMapper mapper = new ObjectMapper();		// POJO to JSON
			
			Client client = Client.create();
			assertNotNull(client);
			client.addFilter(new HTTPBasicAuthFilter(user, password));

	        WebResource webResource = client.resource("http://localhost:8080/pentaho/plugin/tools/api/upload/send");
			assertNotNull(webResource);
			webResource.setProperty(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, false);

			ClientResponse response = webResource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, mapper.writeValueAsString(sendfile));

			assertEquals(200, response.getStatus());
			assertEquals(MediaType.APPLICATION_JSON, response.getType().toString());
			
			// Extract filename from json
//			SendResponse result = mapper.readValue(response.getEntity(String.class), SendResponse.class);
//			assertTrue( result.getTimestamp().length() > 4);
			System.out.println(response.getEntity(String.class));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
