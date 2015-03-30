package com.jsoft.cocimpl.entityengine.impl.pattern;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.PatternNames;
import com.jsoft.cocit.entityengine.PatternAdapter;
import com.jsoft.cocit.entityengine.field.Upload;
import com.jsoft.cocit.util.StringUtil;

public class ImagePatternAdapter implements PatternAdapter {

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
		if (Upload.class.isAssignableFrom(type))
			return new Upload(strFieldValue);

		return strFieldValue;
	}

}
