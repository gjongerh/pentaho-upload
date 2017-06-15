package com.virtorg.bi.sparkl.dto;

import org.json.simple.JSONObject;

import lombok.Data;

@Data
public class FileReceived {

	private String file;
	private String filename;
	private JSONObject data;
}
