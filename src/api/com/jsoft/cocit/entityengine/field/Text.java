package com.jsoft.cocit.entityengine.field;

import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;

@CocColumn(dbColumnDefinition = "text")
public class Text implements IExtField {
	private String text;

	public Text() {
		this("");
	}

	public Text(String t) {
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
