package com.jsoft.cocimpl.dmengine.field;

import org.nutz.json.Json;

import com.jsoft.cocit.dmengine.field.IExtField;
import com.jsoft.cocit.util.StringUtil;

public abstract class JsonField<T> implements IExtField {

	public JsonField() {
		this("");
	}

	public JsonField(String str) {
		if (!StringUtil.isBlank(str)) {
			try {
				T obj = (T) Json.fromJson(this.getClass(), str);
				init(obj);
			} catch (Throwable e) {
			}
		}
	}

	protected abstract void init(T obj);

	public String toString() {
		return Json.toJson(this);
	}

}
