package com.jsoft.cocit.util;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.dmengine.field.RichTextField;
import com.jsoft.cocit.dmengine.field.TextField;
import com.jsoft.cocit.dmengine.field.UploadField;

public abstract class ConstUtil implements Const {

	private static final Map<String, Integer> FIELD_TYPE_MAP = new HashMap();
	static {
		FIELD_TYPE_MAP.put("date", FIELD_TYPE_DATE);
		FIELD_TYPE_MAP.put("boolean", FIELD_TYPE_BOOLEAN);
		FIELD_TYPE_MAP.put("byte", FIELD_TYPE_BYTE);
		FIELD_TYPE_MAP.put("short", FIELD_TYPE_SHORT);
		FIELD_TYPE_MAP.put("integer", FIELD_TYPE_INTEGER);
		FIELD_TYPE_MAP.put("int", FIELD_TYPE_INTEGER);
		FIELD_TYPE_MAP.put("long", FIELD_TYPE_LONG);
		FIELD_TYPE_MAP.put("float", FIELD_TYPE_FLOAT);
		FIELD_TYPE_MAP.put("double", FIELD_TYPE_DOUBLE);
		FIELD_TYPE_MAP.put("bigdecimal", FIELD_TYPE_DECIMAL);
		FIELD_TYPE_MAP.put("number", FIELD_TYPE_NUMBER);
		FIELD_TYPE_MAP.put("string", FIELD_TYPE_STRING);
		FIELD_TYPE_MAP.put(TextField.class.getSimpleName().toLowerCase(), FIELD_TYPE_TEXT);
		FIELD_TYPE_MAP.put(UploadField.class.getSimpleName().toLowerCase(), FIELD_TYPE_UPLOAD);
		FIELD_TYPE_MAP.put(RichTextField.class.getSimpleName().toLowerCase(), FIELD_TYPE_RICHTEXT);
		FIELD_TYPE_MAP.put("list", FIELD_TYPE_ONE2MANY);
	}

	private static final Map<Integer, String> FIELD_TYPECODE_MAP = new HashMap();
	static {
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_DATE, "Date");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_BOOLEAN, "Boolean");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_BYTE, "Byte");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_SHORT, "Short");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_INTEGER, "Integer");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_LONG, "Long");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_FLOAT, "Float");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_DOUBLE, "Double");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_DECIMAL, "BigDecimal");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_NUMBER, "Number");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_STRING, "String");
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_TEXT, TextField.class.getSimpleName());
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_UPLOAD, UploadField.class.getSimpleName());
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_RICHTEXT, RichTextField.class.getSimpleName());
		FIELD_TYPECODE_MAP.put(FIELD_TYPE_ONE2MANY, "List");
	}

	public static int getTypeCode(String type) {
		Integer ret = FIELD_TYPE_MAP.get(type.toLowerCase());
		if (ret == null)
			return 0;

		return ret;
	}

	public static String getType(Integer type) {
		if (type == null)
			return null;

		return FIELD_TYPECODE_MAP.get(type);
	}
}
