package com.jsoft.cocit.dmengine.field;

import com.jsoft.cocit.dmengine.annotation.CocColumn;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;

@CocColumn(dbColumnDefinition = "text")
public class RichTextField implements IExtField {
	private String text;

	public RichTextField() {
		this("");
	}

	public RichTextField(String t) {
		this.text = t;
	}

	public String toString() {
		if (StringUtil.isBlank(text)) {
			return "";
		}
		return text;
	}

	public String toJson() {
		return JsonUtil.toJson(text);
	}
}
