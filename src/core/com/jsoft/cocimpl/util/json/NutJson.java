package com.jsoft.cocimpl.util.json;

import java.io.Writer;

import org.nutz.json.Json;

public class NutJson extends BaseJson {

	
	public String toJson(Object obj) {
		return Json.toJson(obj);
	}

	
	public void toJson(Writer writer, Object obj) {
		Json.toJson(writer, obj);
	}

	
	public <T> T fromJson(String json) {
		return (T) Json.fromJson(json);
	}

	
	public <T> T fromJson(Class<T> type, String json) {
		return Json.fromJson(type, json);
	}

}
