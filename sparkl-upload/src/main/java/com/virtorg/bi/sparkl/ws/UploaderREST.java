package com.virtorg.bi.sparkl.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.simple.JSONObject;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/*
 * UploaderREST is a file uploader extension for Pentaho/Sparkl
 * Heavily inspirend by Marcello Pontes http://blog.oncase.com.br/easy-uploader-for-pentaho-bi-server/ 
 * 
 * Author: Gerard Jongerhuis, g.jongerhuis@virtorg.nl
 * 
 *  With this uploader it is posible to upload a file to the Pentaho BI server. The file is placed under the tomcat folder in temp (in case of ../temp/).
 */

@Path("/{plugin}/api/upload")
public class UploaderREST {
	
	private static final String DIRECTORY = "../temp/";
	private String endpoint;
	
	@GET
	@Path("/ping")
	public String pingPong() {
		return "pong";
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/drop")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response uploadFile(
		@Context UriInfo info
		,@FormDataParam("file") InputStream uploadedInputStream
		,@FormDataParam("file") FormDataContentDisposition fileDetail
			) throws URISyntaxException, UnsupportedEncodingException {

		System.out.println(String.format("Endpoint: %s", endpoint));

		String uploadedFileLocation = DIRECTORY + fileDetail.getFileName();
		File uploadFile = new File(uploadedFileLocation);
		String filePath = "";
		try {
			writeToFile(uploadedInputStream, uploadedFileLocation);
			filePath = uploadFile.getCanonicalPath();
			System.out.println(String.format("filePath: %s", filePath));
		} catch (IOException e) {
			System.out.println("[File Uploader] Error uploading file: "+filePath);
			e.printStackTrace();
			Response.serverError().entity("Error uploading file: "+e.getMessage()).build();
		}

		JSONObject json=new JSONObject();
		json.put("filename", filePath);

		return Response.ok(json.toJSONString(), MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("/send")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
		@Context UriInfo info
		,@FormDataParam("file") InputStream uploadedInputStream
		,@FormDataParam("file") FormDataContentDisposition fileDetail
		,@FormDataParam("endpointPath") String endpointPath
		,@FormDataParam("queryParameters") String queryParameters
			) throws URISyntaxException, UnsupportedEncodingException {

		String uploadedFileLocation = DIRECTORY + fileDetail.getFileName();
		String filePath = new File(uploadedFileLocation).getAbsolutePath();
		
		try {
			writeToFile(uploadedInputStream, uploadedFileLocation);
		} catch (IOException e) {
			System.out.println("[File Uploader] Error uploading file: "+filePath);
			e.printStackTrace();
		}
 
		URI pentahoBaseUrl = info.getBaseUri().resolve("../");
		endpointPath = pentahoBaseUrl + endpointPath;
		endpointPath += "?paramfileUrl="+URLEncoder.encode(filePath,"UTF-8")+queryParameters;
		return Response.temporaryRedirect(new URI(endpointPath)).build();
	}
		
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) throws IOException {
		File dir = new File(DIRECTORY);
		dir.mkdirs();
		File file = new File(uploadedFileLocation);
		file.createNewFile();
		OutputStream out = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = uploadedInputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}

	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}