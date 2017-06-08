package com.virtorg.test.cookbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class JerseyClientPentaho {

	@Test
	public void getClient() {
		try {

	        String user = "Admin";
	        String password = "password";
	        
			Client client = Client.create();
			assertNotNull(client);
			client.addFilter(new HTTPBasicAuthFilter(user, password));

	        WebResource webResource = client.resource("http://localhost:8080/pentaho/plugin/tools/api/upload/ping");
			assertNotNull(webResource);

			ClientResponse response = webResource.accept("text/html").get(ClientResponse.class);
			assertNotNull(response);
			assertEquals(200, response.getStatus());
			
			String output = response.getEntity(String.class);
			assertNotNull(output);
			assertEquals( "pong", output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
