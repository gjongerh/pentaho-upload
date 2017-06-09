package com.virtorg.bi.sparkl.ws;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.virtorg.bi.service.BiServerFileSave;

/*
 * UploaderREST is a file uploader extension for Pentaho/Sparkl
 * Heavily inspirend by Marcello Pontes http://blog.oncase.com.br/easy-uploader-for-pentaho-bi-server/ 
 * 
 * Author: Gerard Jongerhuis, g.jongerhuis@virtorg.nl
 * 
 *  With this uploader it is possible to upload a file to the Pentaho BI server. The file is placed under the tomcat folder in temp (in case of ../temp/).
 */

@Path("/{plugin}/api/upload/send")
public class UploaderSendFile {
	private Logger log = LoggerFactory.getLogger(UploaderSendFile.class);
	
	private static final String FOLDER = "send_temp";
	private String endpoint = "plugin/api/ping";
	
	private BiServerFileSave fs;
	
	public UploaderSendFile() {
		fs = new BiServerFileSave();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response uploadFile(
		@Context UriInfo info
		,@Context HttpServletRequest request
		,@FormDataParam("file") InputStream uploadedInputStream
		,@FormDataParam("file") FormDataContentDisposition fileDetail
		,@FormDataParam("queryParameters") String queryParameters
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
 
		// Request context
		System.out.println(String.format("BaseURI(%s) Uri(%s)...", info.getBaseUri(), info.getAbsolutePath()));
		System.out.println(String.format("endpoint: %s...", info.getBaseUri()+endpoint));
		System.out.println(String.format("Authorization(%s)...", request.getHeader("Authorization")));
		
		// Start processing the file
		Client client = Client.create();
		WebResource webResource = client.resource(info.getBaseUri()+endpoint);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		if(response.getStatus() == 200) {
			// call succeeded send the received content to the caller
			String output = response.getEntity(String.class);
			log.info(output);
			return Response.ok(output, MediaType.APPLICATION_JSON).build();
		} else {
			return Response.status(response.getStatus()).build();
		}
	}
		
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}