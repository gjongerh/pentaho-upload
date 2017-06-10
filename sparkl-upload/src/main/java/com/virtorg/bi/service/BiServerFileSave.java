package com.virtorg.bi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiServerFileSave {
	private Logger log = LoggerFactory.getLogger(BiServerFileSave.class);

	private static final String DIRECTORY = "../temp/";

	/*
	 * writeToFile; save a file to the BI server under the tomcat temp folder
	 * 1- first add DIRECTORY before the filename and add dateTimeStamp
	 * 2- make necessary folders
	 * 3- save the file
	 * 4- return the full filename
	 */
	public String writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {

		//make the new filename
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String newFile = FilenameUtils.getPathNoEndSeparator(uploadedFileLocation) 
				+ "/" +FilenameUtils.getBaseName(uploadedFileLocation) 
				+ "__" + dateFormat.format(date) 
				+ "." + FilenameUtils.getExtension(uploadedFileLocation);
		log.debug(newFile);
		
		File file = new File(DIRECTORY + newFile);
		log.debug(String.format("Filename: %s", file.getCanonicalPath()));
		
		// find parent folder of the file and make sure that it exist
		File dir = file.getParentFile();
		log.debug(String.format("ParentFolder: %s", dir.getCanonicalPath()));
		dir.mkdirs();
		
		// create the file
		file.createNewFile();
		OutputStream out = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = uploadedInputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
		
		return file.getCanonicalPath();
	}
}
