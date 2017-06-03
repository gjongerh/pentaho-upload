package com.virtorg.bi.tst.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

@Component
@Path("/upload")
public class UploadFileREST {
	
	public static final String DIRECTORY = "./work";

    @GET
    @Path("/hello")
    public String hello() {
        return "Hello World";
    }

	
//	@POST
//	@Path("/file")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public Response uploadFile(
//			@Context UriInfo info
//			,@FormDataParam("file") InputStream uploadedInputStream
//			,@FormDataParam("file") FormDataContentDisposition fileDetail
//	
////			,@FormDataParam("endpointPath") String endpointPath
////			,@FormDataParam("queryParameters") String queryParameters
//			) throws URISyntaxException, UnsupportedEncodingException {
//		
//		String uploadedFileLocation = DIRECTORY + fileDetail.getFileName();
//		String filePath = new File(uploadedFileLocation).getAbsolutePath();
//		
//		try {
//			writeToFile(uploadedInputStream, uploadedFileLocation);
//		} catch (IOException e) {
//			System.out.println("[File Uploader] Error uploading file: "+filePath);
//			e.printStackTrace();
//		}
// 
////		URI pentahoBaseUrl = info.getBaseUri().resolve("../");
////		endpointPath = pentahoBaseUrl + endpointPath;
////		endpointPath += "?paramfileUrl="+URLEncoder.encode(filePath,"UTF-8")+queryParameters;
////
////		return Response.temporaryRedirect(new URI(endpointPath)).build();
//		
//		return Response.ok().build();
//	}
//	
	
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

}