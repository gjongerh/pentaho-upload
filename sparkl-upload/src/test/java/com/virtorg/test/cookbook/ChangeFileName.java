package com.virtorg.test.cookbook;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

public class ChangeFileName {

	@Test
	public void changeFileName() {
		
		File pdf = new File("work/eurotext.tif");
		assertNotNull(pdf);
		System.out.println(pdf.getAbsolutePath());
		
		String baseName = FilenameUtils.getBaseName(pdf.getAbsolutePath());
		assertNotNull(baseName);
		System.out.println("BaseName: "+baseName);
		
		String extension = FilenameUtils.getExtension(pdf.getAbsolutePath());
		assertNotNull(extension);
		System.out.println("Extension: "+extension);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		assertNotNull(dateFormat);
		Date date = new Date();
		assertNotNull(date);
		System.out.println(dateFormat.format(date));

		String newFile = baseName + "__" + dateFormat.format(date) + "." + extension;
		assertNotNull(newFile);
		System.out.println("New filename: "+newFile);
	}
}
