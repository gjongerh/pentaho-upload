package com.virtorg.bi.sparkl.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
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
import com.virtorg.bi.service.BiServerFileSave;

/*
 * UploaderREST is a file uploader extension for Pentaho/Sparkl
 * Heavily inspirend by Marcello Pontes http://blog.oncase.com.br/easy-uploader-for-pentaho-bi-server/ 
 * 
 * Author: Gerard Jongerhuis, g.jongerhuis@virtorg.nl
 * 
 *  With this uploader it is possible to upload a file to the Pentaho BI server. The file is placed under the tomcat folder in temp (in case of ../temp/).
 */

@Path("/{plugin}/api/upload/drop")
public class UploaderDropFile {
	
	private String folder = "folder";
	private BiServerFileSave fs;
	
	public UploaderDropFile() {
		fs = new BiServerFileSave();
	}
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Response uploadFile(
		@Context UriInfo info
		,@FormDataParam("file") InputStream uploadedInputStream
		,@FormDataParam("file") FormDataContentDisposition fileDetail
			) throws URISyntaxException, UnsupportedEncodingException {

		String uploadedFileLocation = folder + "/" + fileDetail.getFileName();
		String filePath = "";
		try {
			filePath = fs.writeToFile(uploadedInputStream, uploadedFileLocation);
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
	
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
}