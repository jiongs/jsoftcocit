package com.jsoft.cocit.dmengine.field;

import com.jsoft.cocit.dmengine.annotation.CocColumn;
import com.jsoft.cocit.util.JsonUtil;

@CocColumn(precision = 255)
public class UploadField implements IExtField {

	private String path;

	public UploadField() {
		this("");
	}

	public UploadField(String str) {
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
