package com.virtorg.bi.sparkl.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataParam;
import com.virtorg.bi.service.BiServerFileSave;
import com.virtorg.bi.sparkl.dto.FileReceived;
import com.virtorg.bi.sparkl.dto.SendResponse;

/*
 * UploaderREST is a file uploader extension for Pentaho/Sparkl
 * Heavily inspirend by Marcello Pontes http://blog.oncase.com.br/easy-uploader-for-pentaho-bi-server/ 
 * 
 * Author: Gerard Jongerhuis, g.jongerhuis@virtorg.nl
 * 
 * With this uploader it is possible to upload a file to the Pentaho BI server. The file is placed under the tomcat folder in temp (in case of ../temp/).
 *  
 * Receiving a file and JSON data
 * entries in the JSON that start with "param" will be used as a parameter in calling the Pentaho Endpoint  
 */

@Path("/{plugin}/api/upload/send")
public class UploaderSendFile {
	private Logger log = LoggerFactory.getLogger(UploaderSendFile.class);
	
	private static final String FOLDER = "send_temp";
	private String endpoint = "plugin/api/ping";
	private String user = "admin";
	private String password = "password";
	
	private BiServerFileSave fs;
	
	public UploaderSendFile() {
		fs = new BiServerFileSave();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(	@Context UriInfo info, @Context HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();		// POJO to JSON
		FileReceived fileReceived = mapper.readValue(request.getInputStream(), FileReceived.class);
		//System.out.println("Filename: "+fileReceived.getFilename());
		
		String uploadedFileLocation = FOLDER + "/" + fileReceived.getFilename();
		String filePath = "";
		
		try {
			filePath = fs.writeToFile( new ByteArrayInputStream(Base64.decode(fileReceived.getFile())), uploadedFileLocation);
		} catch (IOException e) {
			log.error("[File Uploader] Error uploading file: "+filePath);
			e.printStackTrace();

			return Response.notModified("ERROR uploading file due to => "+e.getMessage()).build();
		}

		ClientResponse response = processRequest(info, request, filePath, fileReceived.getData());
		
		SendResponse result = new SendResponse();
		if(response.getStatus() == 200) {
			// call succeeded send the received content to the caller
			JSONObject resultData = new JSONObject();
			try {
				resultData = mapper.readValue(response.getEntity(String.class), JSONObject.class);
				//System.out.println(resultData.toJSONString());
				log.debug(resultData.toJSONString());
			} catch (Exception e) {
				result.setMessage("ERROR: Result of endpoint was not parseble JSON...");
				return Response.ok(mapper.writeValueAsString(result), MediaType.APPLICATION_JSON).build();
			}
			
			result.setData(resultData);
			result.setMessage("successful");

			return Response.ok(mapper.writeValueAsString(result), MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(response.getStatus()).build();
		}

	}
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(
		@Context UriInfo info
		,@Context HttpServletRequest request
		,@FormDataParam("file") InputStream uploadedInputStream
		,@FormDataParam("file") FormDataContentDisposition fileDetail
		,@FormDataParam("data") String data
			) throws URISyntaxException, UnsupportedEncodingException {

		String uploadedFileLocation = FOLDER + "/" + fileDetail.getFileName();
		String filePath = "";
		
		try {
			filePath = fs.writeToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			log.error("[File Uploader] Error uploading file: "+filePath);
			e.printStackTrace();
			return Response.notModified(e.getMessage()).build();
		}
 
		ClientResponse response = processRequest(info, request, filePath, null);
		
		if(response.getStatus() == 200) {
			// call succeeded send the received content to the caller
			String output = response.getEntity(String.class);
			log.debug(output);
			return Response.ok(output, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(response.getStatus()).build();
		}
	}

	private ClientResponse processRequest(UriInfo info, HttpServletRequest request, String filePath, JSONObject data) {
		// Start processing the file
		log.debug("Entry: processRequest...");
		Client client = Client.create();

		// if basic authorization exist reuse it in the new request
		if(request.getHeader("Authorization") != null) {
			String auth = request.getHeader("Authorization");
			String decoded = Base64.base64Decode(auth.split(" ")[1]);
			String[] userPassword = decoded.split(":");
			client.addFilter(new HTTPBasicAuthFilter(userPassword[0], userPassword[1]));
		} else {
			client.addFilter(new HTTPBasicAuthFilter( user, password));
		}

		String url = "";
		if(endpoint.startsWith("http")) {
			url = endpoint;
		} else {
			url = info.getBaseUri()+endpoint;
		}
		log.debug(String.format("Using endpoint(%s)...", url));
		//System.out.println(String.format("Using endpoint(%s)...", url));

		// Create GET parameters for the request
		MultivaluedMapImpl params = new MultivaluedMapImpl();
		params.add("paramFilename", filePath);
		if(data != null) {
			log.debug(data.toJSONString());
			for (Object key : data.keySet()) {
				String keyStr = (String)key;
				if(keyStr.startsWith("param")) {
					params.add(keyStr, data.get(key));
				}
			}
		}
		
		ClientResponse response = client
			.resource(url)
			.queryParams(params)
			.accept(MediaType.APPLICATION_JSON)
			.get(ClientResponse.class);
		return response;
	}
		
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}