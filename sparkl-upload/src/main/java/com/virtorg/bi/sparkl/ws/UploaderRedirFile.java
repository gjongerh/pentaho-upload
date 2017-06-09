package com.virtorg.bi.sparkl.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@Path("/{plugin}/api/upload/redir")
public class UploaderRedirFile {
	private Logger log = LoggerFactory.getLogger(UploaderRedirFile.class);
	
	private static final String FOLDER = "send_temp";
	private String endpoint = "pentaho/plugin";
	
	private BiServerFileSave fs;
	
	public UploaderRedirFile() {
		fs = new BiServerFileSave();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
		@Context UriInfo info
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
		}
 
		URI pentahoBaseUrl = info.getBaseUri().resolve("../");
		String endpointPath = pentahoBaseUrl + endpoint;
		endpointPath += "?paramfileUrl="+URLEncoder.encode(filePath,"UTF-8")+queryParameters;
		return Response.temporaryRedirect(new URI(endpointPath)).build();
	}
		
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}