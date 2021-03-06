package com.jsoft.cocimpl.dmengine.impl.pattern;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.PatternNames;
import com.jsoft.cocit.dmengine.IPatternAdapter;
import com.jsoft.cocit.dmengine.field.UploadField;
import com.jsoft.cocit.util.StringUtil;

public class ImagePatternAdapter implements IPatternAdapter {

	private static final String DEFAULT_PATTERN = "*.jpg;*.gif;*.png";

	private static final String CONFIG_KEY = "pattern." + PatternNames.PATTERN_IMAGE;

	@Override
	public String getName() {
		return PatternNames.PATTERN_IMAGE;
	}

	@Override
	public String getPattern() {
		Cocit coc = Cocit.me();
		ICocConfig commonConfig = coc.getConfig();

		String format = commonConfig.get(CONFIG_KEY, DEFAULT_PATTERN);

		if (StringUtil.isBlank(format)) {
			return DEFAULT_PATTERN;
		}

		return format;
	}

	@Override
	public boolean validate(Object fieldValue) {
		return true;
	}

	@Override
	public String format(Object fieldValue) {
		if (fieldValue == null)
			return "";

		return fieldValue.toString();
	}

	@Override
	public Object parse(String strFieldValue, Class type) {
		if (UploadField.class.isAssignableFrom(type))
			return new UploadField(strFieldValue);

		return strFieldValue;
	}

}
