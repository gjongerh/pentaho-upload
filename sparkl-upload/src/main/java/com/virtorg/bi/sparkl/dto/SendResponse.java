package com.virtorg.bi.sparkl.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

import lombok.Data;

@Data
public class SendResponse {

	public SendResponse() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		this.timestamp = dateFormat.format(date);
	}
	public SendResponse(String message) {
		this();
		this.message = message;
	}
	
	private String message;
	private String timestamp;
	private JSONObject data;
}
