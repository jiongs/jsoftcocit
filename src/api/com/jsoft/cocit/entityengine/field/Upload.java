package com.jsoft.cocit.entityengine.field;

import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.util.JsonUtil;

@CocColumn(precision = 255)
public class Upload implements IExtField {

	private String path;

	public Upload() {
		this("");
	}

	public Upload(String str) {
		if (str == null)
			this.path = "";
		else
			this.path = str;
	}

	public String getPath() {
		return path;
	}

	public String toString() {
		return path;
	}

	public String toJson() {
		return JsonUtil.toJson(path);
	}

}
