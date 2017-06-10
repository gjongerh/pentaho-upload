package com.virtorg.test.cookbook;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.jersey.core.util.Base64;

public class BasicAuthDecode {

	/*
	 * The Basic Authorization header is just a Base64 encoded string "user:password"
	 */
	@Test
	public void basicAutoDecode() {
		
		String auth = "Basic YWRtaW46cGFzc3dvcmQ=";
		assertNotNull(auth);
		
		String code = auth.split(" ")[1];
		assertNotNull(code);
		System.out.println(code);
		
		String decode = Base64.base64Decode(code);
		assertNotNull(decode);
		System.out.println(decode);
		
		String[] decoded = decode.split(":");
		assertNotNull(decoded);
		System.out.println(decoded[0]+" "+decoded[1]);
	}
}
