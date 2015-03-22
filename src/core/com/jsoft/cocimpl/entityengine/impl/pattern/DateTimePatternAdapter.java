package com.jsoft.cocimpl.entityengine.impl.pattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICommonConfig;
import com.jsoft.cocit.constant.PatternNames;
import com.jsoft.cocit.entityengine.PatternAdapter;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.StringUtil;

public class DateTimePatternAdapter implements PatternAdapter<Date> {

	private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final String CONFIG_KEY = "pattern.date";

	@Override
	public String getName() {
		return PatternNames.PATTERN_DATETIME;
	}

	@Override
	public String getPattern() {
		Cocit coc = Cocit.me();
		ICommonConfig commonConfig = coc.getConfig();

		String format = commonConfig.get(CONFIG_KEY, DEFAULT_PATTERN);

		if (StringUtil.isBlank(format)) {
			return DEFAULT_PATTERN;
		}

		return format;
	}

	@Override
	public boolean validate(Date fieldValue) {
		return true;
	}

	@Override
	public String format(Date fieldValue) {
		if (fieldValue == null) {
			return "";
		}

		Cocit coc = Cocit.me();
		ICommonConfig commonConfig = coc.getConfig();

		String format = commonConfig.get(CONFIG_KEY, DEFAULT_PATTERN);

		return (new SimpleDateFormat(format)).format(fieldValue);
	}

	@Override
	public Date parse(String strFieldValue, Class<Date> type) {
		if (strFieldValue == null || strFieldValue.trim().length() == 0) {
			return null;
		}

		Cocit coc = Cocit.me();
		ICommonConfig commonConfig = coc.getConfig();
		String format = commonConfig.get(CONFIG_KEY, DEFAULT_PATTERN);

		try {
			return new SimpleDateFormat(format).parse(strFieldValue.trim());
		} catch (ParseException e) {
			throw new CocException(e);
		}
	}

}
